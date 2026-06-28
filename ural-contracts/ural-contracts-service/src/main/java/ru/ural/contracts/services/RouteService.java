package ru.ural.contracts.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.contracts.clients.GraphHopperClient;
import ru.ural.contracts.clients.dto.GraphHopperGeocodeResponse;
import ru.ural.contracts.clients.dto.GraphHopperRouteResponse;
import ru.ural.contracts.entities.Contract;
import ru.ural.contracts.entities.Route;
import ru.ural.contracts.entities.RoutePoint;
import ru.ural.contracts.properties.GraphHopperProperties;
import ru.ural.contracts.repositories.RouteRepository;
import ru.ural.dto.AddressDto;
import ru.ural.exceptions.BadRequestException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final GraphHopperClient graphHopperClient;

    private final GraphHopperProperties graphHopperProperties;

    private final RouteRepository routeRepository;

    private final ObjectMapper objectMapper;

    public Route buildRoute(Contract contract, CargoDto cargo) {
        if (!StringUtils.hasText(graphHopperProperties.getApiKey())) {
            throw new BadRequestException("GraphHopper API key is not configured");
        }

        String fromAddress = formatAddress(cargo.getLoadingPlace());
        String toAddress = formatAddress(cargo.getUnloadingPlace());
        GraphHopperGeocodeResponse.GraphHopperPoint fromPoint = geocode(fromAddress, "погрузки");
        GraphHopperGeocodeResponse.GraphHopperPoint toPoint = geocode(toAddress, "разгрузки");

        GraphHopperRouteResponse routeResponse = graphHopperClient.buildRoute(
                fromPoint.getLat(),
                fromPoint.getLng(),
                toPoint.getLat(),
                toPoint.getLng()
        );
        GraphHopperRouteResponse.GraphHopperPath path = getFirstPath(routeResponse);
        List<RoutePoint> points = toRoutePoints(path);
        ZonedDateTime now = ZonedDateTime.now();

        Route route = routeRepository.findByContractId(contract.getId())
                .orElseGet(() -> Route.builder()
                        .contract(contract)
                        .createdAt(now)
                        .build());

        route.setContract(contract);
        route.setPoints(points);
        route.setDistanceMeters(path.getDistance());
        route.setTimeMs(path.getTime());
        route.setUpdatedAt(route.getId() == null ? null : now);
        route.setRawResponse(buildRawResponse(fromAddress, toAddress, fromPoint, toPoint, routeResponse));

        Route savedRoute = routeRepository.save(route);
        contract.setRoute(savedRoute);
        return savedRoute;
    }

    private GraphHopperGeocodeResponse.GraphHopperPoint geocode(String address, String pointName) {
        GraphHopperGeocodeResponse response = graphHopperClient.geocode(address);
        if (response == null || CollectionUtils.isEmpty(response.getHits())) {
            throw new BadRequestException("GraphHopper не смог определить координаты адреса %s: %s"
                    .formatted(pointName, address));
        }

        GraphHopperGeocodeResponse.GraphHopperPoint point = response.getHits().get(0).getPoint();
        if (point == null || point.getLat() == null || point.getLng() == null) {
            throw new BadRequestException("GraphHopper вернул некорректные координаты адреса %s: %s"
                    .formatted(pointName, address));
        }

        return point;
    }

    private GraphHopperRouteResponse.GraphHopperPath getFirstPath(GraphHopperRouteResponse response) {
        if (response == null || CollectionUtils.isEmpty(response.getPaths())) {
            throw new BadRequestException("GraphHopper не смог построить маршрут");
        }

        return response.getPaths().get(0);
    }

    private List<RoutePoint> toRoutePoints(GraphHopperRouteResponse.GraphHopperPath path) {
        List<List<BigDecimal>> coordinates = path.getPoints() == null ? null : path.getPoints().getCoordinates();
        if (CollectionUtils.isEmpty(coordinates)) {
            throw new BadRequestException("GraphHopper вернул маршрут без координат");
        }

        List<RoutePoint> points = coordinates.stream()
                .filter(coordinate -> coordinate != null && coordinate.size() >= 2)
                .map(coordinate -> RoutePoint.builder()
                        .longitude(coordinate.get(0))
                        .latitude(coordinate.get(1))
                        .build())
                .toList();

        if (points.isEmpty()) {
            throw new BadRequestException("GraphHopper вернул маршрут без корректных координат");
        }

        return points;
    }

    private Map<String, Object> buildRawResponse(
            String fromAddress,
            String toAddress,
            GraphHopperGeocodeResponse.GraphHopperPoint fromPoint,
            GraphHopperGeocodeResponse.GraphHopperPoint toPoint,
            GraphHopperRouteResponse routeResponse
    ) {
        Map<String, Object> rawResponse = new LinkedHashMap<>();
        rawResponse.put("fromAddress", fromAddress);
        rawResponse.put("toAddress", toAddress);
        rawResponse.put("fromPoint", objectMapper.convertValue(fromPoint, new TypeReference<Map<String, Object>>() {
        }));
        rawResponse.put("toPoint", objectMapper.convertValue(toPoint, new TypeReference<Map<String, Object>>() {
        }));
        rawResponse.put("route", objectMapper.convertValue(routeResponse, new TypeReference<Map<String, Object>>() {
        }));
        return rawResponse;
    }

    private String formatAddress(AddressDto address) {
        if (address == null) {
            throw new BadRequestException("Для построения маршрута не указан адрес");
        }

        List<String> parts = Stream.of(
                        address.getCountry(),
                        address.getCity(),
                        address.getStreet(),
                        address.getHouse(),
                        address.getApartment(),
                        address.getPostalCode()
                )
                .filter(StringUtils::hasText)
                .toList();

        if (parts.isEmpty()) {
            throw new BadRequestException("Для построения маршрута указан пустой адрес");
        }

        return String.join(", ", parts);
    }

}
