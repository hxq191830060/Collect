import {
  User,
  UserFilled,
  ArrowLeftBold,
  TrendCharts,
  Histogram,
  CaretBottom,
  Edit,
  Upload,
  Link,
  InfoFilled,
  Plus,
  Delete,
  CirclePlus,
  Failed,
  Promotion,
  Connection,
  SuccessFilled,
} from "@element-plus/icons-vue";

const icons = [
  User,
  UserFilled,
  ArrowLeftBold,
  TrendCharts,
  Histogram,
  CaretBottom,
  Edit,
  Upload,
  Link,

  InfoFilled,
  Plus,
  Delete,
  CirclePlus,
  SuccessFilled,
  Failed,
  Promotion,
  Connection,
];

export default function registerIcon(app) {
  icons.forEach((icon) => {
    app.component(icon.name, icon);
  });
}
