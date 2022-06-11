//区分线上环境和生产环境的请求API的url
let VUE_APP_MODE = process.env.VUE_APP_MODE;
let BASE_URL = "";
if (VUE_APP_MODE === "production") BASE_URL = "http://150.158.158.176:30850/";
if (VUE_APP_MODE === "development") BASE_URL = "http://150.158.158.176:30850/";
export default BASE_URL;
