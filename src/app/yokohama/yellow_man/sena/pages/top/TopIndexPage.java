package yokohama.yellow_man.sena.pages.top;

import java.io.Serializable;
import java.util.List;

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
}
