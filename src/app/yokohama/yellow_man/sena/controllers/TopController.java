package yokohama.yellow_man.sena.controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Topページコントローラ。
 *
 * @author yellow-man
 * @since 1.0
 */
public class TopController extends Controller {

	/**
	 * Topページアクション。
	 * @return
	 */
	public static Result index() {
		return ok(views.html.top.index.render());
	}
}
