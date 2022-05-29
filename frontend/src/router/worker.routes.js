import { UserRole } from "@/enums/user/userRole";

export default [
  {
    meta: { permission: [UserRole.WORKER] },
    path: "/worker-square",
    name: "worker-square",
    component: () =>
      import(/* webpackChunkName: "worker" */ "@/views/Worker/WorkerSquare"),
  },
  {
    meta: { permission: [UserRole.WORKER] },
    path: "/worker-task/:taskId",
    name: "worker-task",
    component: () =>
      import(/* webpackChunkName: "worker" */ "@/views/Worker/WorkerTask"),
    props: (route) => ({
      taskId: route.params.taskId,
      publisherId: route.query.publisherId,
    }),
  },
  {
    meta: { permission: [UserRole.WORKER] },
    path: "/worker-platform",
    name: "worker-platform",
    component: () =>
      import(/* webpackChunkName: "worker" */ "@/views/Worker/WorkerPlatform"),
  },
];
