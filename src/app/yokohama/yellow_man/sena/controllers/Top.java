package yokohama.yellow_man.sena.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Top extends Controller {

    public static Result index() {
        return ok(index.render());
    }

}
