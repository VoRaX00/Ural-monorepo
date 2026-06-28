FROM node:20-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN if [ -f package-lock.json ]; then npm ci; else npm install; fi

COPY public ./public
COPY src ./src
COPY docker ./docker
COPY tsconfig.json ./

RUN test -f src/App.tsx || (find src -maxdepth 2 -type f | sort && false)
RUN npm run build

# ---Main---
FROM nginx:alpine

COPY --from=builder /app/build /usr/share/nginx/html

COPY docker/default.conf /etc/nginx/conf.d/default.conf
COPY docker/10-runtime-config.sh /docker-entrypoint.d/10-runtime-config.sh
RUN chmod +x /docker-entrypoint.d/10-runtime-config.sh

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
