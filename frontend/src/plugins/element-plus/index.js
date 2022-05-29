import registerElement from "./register-element";
import registerIcon from "./register-icon";
import "element-plus/dist/index.css";

export default function globalComponentRegister(app) {
  registerElement(app);
  registerIcon(app);
}
