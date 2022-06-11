import { UserRole } from "@/enums/user/userRole";

export default [
  {
    meta: { permission: [UserRole.ADMIN, UserRole.PUBLISHER, UserRole.WORKER] },
    path: "/login",
    name: "login",
    props: (route) => ({ mode: route.query.mode }),
    component: () =>
      import(/* webpackChunkName: "login" */ "@/views/Login/Login"),
  },
];
