import BASE_URL from "@/enums/config/url";
import axios from "axios";

let config = {
  baseURL: BASE_URL,
  timeout: 60 * 1000,
};
const request = axios.create(config);
export default request;
