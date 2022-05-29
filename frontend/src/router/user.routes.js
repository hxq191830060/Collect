export default [
  {
    path: "/user",
    name: "user",
    component: () => import(/* webpackChunkName: "user-info" */ "@/views/User/User"),
    props: (route) => ({
      tabName: route.query.tabName,
    }),
  },
];
