package yokohama.yellow_man.sena.api.params;

import java.util.List;
import java.util.Map;

/**
 * DataTablesパラメータマッピングクラス。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 * @see <a href="https://datatables.net/manual/server-side">Server-side processing https://datatables.net/manual/server-side</a>
 */
public class DataTablesParams extends AppParams {

	/** マップキー：search[value] */
	public static final String MAP_KEY_SEARCH_VALUE = "value";

	/**
	 * ※翻訳サイト参照：カウンターを描く。
	 * <p>これは、DataTableによって使用され、サーバー側の処理要求からのAjax戻り値がDataTableによって順番に引き出されるようにします（Ajax要求は非同期であり、順序が乱れることがあります）。これはdraw returnパラメータの一部として使用されます。
	 */
	public Integer draw;

	/**
	 * ※翻訳サイト参照：最初のレコードインジケータを呼び出します。
	 * <p>これは現在のデータセットの開始点です（0インデックスベース、つまり0が最初のレコードです）。
	 */
	public Integer start;

	/**
	 * ※翻訳サイト参照：現在の描画でテーブルが表示できるレコードの数。
	 * <p>サーバが返すレコードが少ない場合を除いて、返されるレコードの数はこの数に等しいと予想されます。これは、すべてのレコードを返す必要があることを示すために-1にすることができることに注意してください（サーバ側の処理のメリットは無効になります）。
	 */
	public Integer length;

	/**
	 * TODO yellow-man 全文検索はページングがあるため、テーブルのヒモ付なども考慮しなければならないので
	 * 1.1.0-1.1においては、銘柄（stocks）情報のみの検索とする。（銘柄コード、銘柄名のみ。）
	 * <p>※翻訳サイト参照：
	 * <ul>
	 *     <li>search[value] （string）
	 *         <p>グローバル検索値検索可能なすべての列に適用されます。</li>
	 *     <li>search[regex] （boolean）
	 *         <p>グローバルフィルタを高度な検索のための正規表現として扱う必要がある場合はtrue、そうでない場合はfalse通常、サーバー側の処理スクリプトは、大量のデータセットでパフォーマンス上の理由で正規表現の検索を実行しませんが、技術的に可能であり、スクリプトの裁量で行うことができます。</li>
	 * </ul>
	 */
	public Map<String, String> search;

	/**
	 * ※翻訳サイト参照：
	 * <ul>
	 *     <li>order[i][column] （integer）
	 *         <p>順序を適用する列。これは、サーバーにも送信される情報の列配列へのインデックス参照です。</li>
	 *     <li>order[i][dir] （string）
	 *         <p>この列の注文方向。それぞれ昇順または降順を示すにはascまたはdescになります。</li>
	 * </ul>
	 */
	public List<Map<String, String>> order;

	/**
	 * TODO yellow-man 初段では使わない想定。
	 * <p>※翻訳サイト参照：
	 * <ul>
	 *     <li>columns[i][data] （integer）
	 *         <p>columns.dataで定義されている列のデータソース。</li>
	 *     <li>columns[i][name] （string）
	 *         <p>columns.nameで定義されている列名。</li>
	 *     <li>columns[i][searchable] （boolean）
	 *         <p>この列が検索可能（true）か検索不可（false）かを示すフラグ。これはcolumns.searchableによって制御されます。</li>
	 *     <li>columns[i][orderable] （boolean）
	 *         <p>この列が順序付け可能かどうかを示すフラグです（trueまたはfalse）。これはcolumns.orderableによって制御されます。</li>
	 *     <li>columns[i][searchValue] （string）
	 *         <p>この特定の列に適用する値を検索します。</li>
	 *     <li>columns[i][searchRegex] （boolean）
	 *         <p>この列の検索キーワードを正規表現（true）または非正規表現（false）として扱うかどうかを示すフラグ。グローバル検索の場合と同様に、通常はサーバー側の処理スクリプトは、大規模なデータセットでパフォーマンス上の理由で正規表現検索を実行しませんが、技術的に可能であり、スクリプトの裁量により可能です。</li>
	 * </ul>
	 */
	public List<Map<String, String>> columns;
}
