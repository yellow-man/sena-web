package yokohama.yellow_man.sena.api.params;

import java.util.List;
import java.util.Map;

/**
 * DataTablesパラメータマッピングクラス。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 * @see https://datatables.net/
 */
public class DataTablesParams extends AppParams {

	public Integer draw;
	public Integer start;
	public Integer length;
	public Map<String, String> search;
	public List<Map<String, String>> order;
	public List<Map<String, String>> columns;
}
