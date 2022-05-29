import { UserRole } from "@/enums/user/userRole";

export default [
  {
    meta: { permission: [UserRole.ADMIN] },
    path: "/admin-square",
    name: "admin-square",
    component: () => import(/* webpackChunkName: "admin" */ "@/views/Admin/AdminSquare"),
  },
  {
    meta: { permission: [UserRole.ADMIN] },
    path: "/admin-task",
    name: "admin-task",
    component: () => import(/* webpackChunkName: "admin" */ "@/views/Admin/AdminTask"),
    props: (route) => ({
      taskId: route.params.taskId,
      publisherId: route.query.publisherId,
    }),
  },
  {
    path: "/admin-bomb",
    name: "admin-bomb",
    component: () => import(/* webpackChunkName: "admin" */ "@/views/Admin/BombMailboxes"),
  },
];
