import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import globalComponentRegister from "./plugins/element-plus/index";

createApp(App).use(store).use(router).use(globalComponentRegister).mount("#app");
