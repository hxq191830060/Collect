import { UserRole } from "@/enums/user/userRole";

export default [
  {
    meta: { permission: [UserRole.PUBLISHER] },
    path: "/publisher-square",
    name: "publisher-square",
    component: () =>
      import(/* webpackChunkName: "publisher" */ "@/views/Publisher/PublisherSquare"),
  },
  {
    meta: { permission: [UserRole.PUBLISHER] },
    path: "/publisher-task/:taskId",
    name: "publisher-task",
    component: () => import(/* webpackChunkName: "publisher" */ "@/views/Publisher/PublisherTask"),
    props: (route) => ({
      taskId: route.params.taskId,
      publisherId: route.query.publisherId,
    }),
  },
  {
    meta: { permission: [UserRole.PUBLISHER] },
    path: "/publisher-platform",
    name: "publisher-platform",
    component: () =>
      import(/* webpackChunkName: "publisher" */ "@/views/Publisher/PublisherPlatform"),
  },
  {
    meta: { permission: [UserRole.PUBLISHER] },
    path: "/report-summary/:taskId",
    name: "report-summary",
    component: () => import(/* webpackChunkName: "publisher" */ "@/views/Publisher/ReportSummary"),
    props: (route) => ({
      taskId: route.params.taskId,
    }),
  },
  {
    meta: { permission: [UserRole.PUBLISHER] },
    path: "/report-detail/:testId",
    name: "report-detail",
    component: () => import(/*webpackChunkName: "publisher"*/ "@/views/Publisher/ReportDetail"),
    props: (route) => ({
      testId: route.params.testId,
    }),
  },
];
