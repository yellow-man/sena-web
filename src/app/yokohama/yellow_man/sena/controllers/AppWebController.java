package yokohama.yellow_man.sena.controllers;

import play.mvc.Controller;
import yokohama.yellow_man.sena.annotations.ParamsTrace;
import yokohama.yellow_man.sena.annotations.PerformanceCheck;

/**
 * Webコントローラー基底クラス。
 * <p>Webを実装する場合、このクラスを継承する。
 *
 * @author yellow-man
 * @since 1.0.0-1.0
 */
@PerformanceCheck
@ParamsTrace
public class AppWebController extends Controller {
}
