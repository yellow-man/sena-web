package yokohama.yellow_man.sena.pages.top;

import java.io.Serializable;

import yokohama.yellow_man.sena.pages.AppPage;

/**
 * Topページ情報保持クラス。
 *
 * @author yellow-man
 * @since 1.0.0-1.0
 * @version 1.1.0-1.1
 */
@SuppressWarnings("serial")
public class TopIndexPage extends AppPage implements Serializable {

	/** 銘柄リスト：取得日 */
	public String stocksDateStr;

	/** 指標マップ：取得日 */
	public String indicatorsDateStr;

	/** 銘柄リスト：公表日 */
	public String debitBalancesReleaseDateStr;
}
