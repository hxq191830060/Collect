import { UserRole } from "@/enums/user/userRole";

export default [
  {
    meta: { permission: [UserRole.ADMIN, UserRole.PUBLISHER, UserRole.WORKER] },
    path: "/collaboration-report/:id",
    name: "collaboration-report",
    props: (route) => ({ id: route.params.id }),
    component: () =>
      import(/* webpackChunkName: "login" */ "@/views/Collaboration/CollaborationReport"),
  },
];
