import { createRouter, createWebHashHistory } from "vue-router";
import Home from "../views/Home/Home.vue";
import { UserRole } from "@/enums/user/userRole";

const routerList = [];

function importAllRoutes(r) {
  r.keys().forEach((path) => {
    routerList.push(...r(path).default);
  });
}

importAllRoutes(require.context("./", true, /\.routes\.js/));

const routes = [
  ...routerList,
  {
    meta: {
      permission: [UserRole.WORKER, UserRole.ADMIN, UserRole.PUBLISHER],
    },
    path: "/",
    name: "home",
    component: Home,
  },
  {
    path: "/index",
    redirect: "/",
  },
  {
    path: "/home",
    redirect: "/",
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
