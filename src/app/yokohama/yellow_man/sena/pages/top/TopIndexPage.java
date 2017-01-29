package yokohama.yellow_man.sena.pages.top;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import yokohama.yellow_man.sena.core.models.DebitBalances;
import yokohama.yellow_man.sena.core.models.Indicators;
import yokohama.yellow_man.sena.core.models.Stocks;
import yokohama.yellow_man.sena.pages.AppPage;

/**
 * Topページ情報保持クラス。
 *
 * @author yellow-man
 * @since 1.0
 */
@SuppressWarnings("serial")
public class TopIndexPage extends AppPage implements Serializable {

	/** 銘柄リスト */
	public List<Stocks> stocks;
	/** 指標マップ <銘柄コード, 指標> */
	public Map<Integer, Indicators> indicatorsMap;
	/** 信用残マップ <銘柄コード, 信用残> */
	public Map<Integer, DebitBalances> debitBalancesMap;
}
