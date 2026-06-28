import type { RcFile } from "antd/es/upload";
import type { UploadFile } from "antd/es/upload/interface";

export const extractRcFiles = (fileList: UploadFile[]): RcFile[] =>
  fileList
    .map((file) => {
      const rawFile = file.originFileObj ?? (file as unknown);
      return rawFile instanceof File ? (rawFile as RcFile) : null;
    })
    .filter((file): file is RcFile => file !== null);
