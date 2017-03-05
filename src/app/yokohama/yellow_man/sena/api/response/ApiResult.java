package yokohama.yellow_man.sena.api.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

/**
 * API結果格納クラス。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 */
public class ApiResult {

	/** 処理結果 **/
	private Integer result;

	/** コンテンツ **/
	private Map<String, Object> content;


	/**
	 * 処理結果{@code result}で初期化したコンストラクタ。
	 * @param result 処理結果
	 * @since 1.1.0-1.1
	 */
	public ApiResult(Integer result) {
		super();
		this.result = result;
		this.content = new HashMap<String, Object>();
	}

	/**
	 * 処理結果{@code result}、コンテンツ{@code content}で初期化したコンストラクタ。
	 * @param result 処理結果
	 * @param content コンテンツ
	 * @since 1.1.0-1.1
	 */
	public ApiResult(Integer result, Map<String, Object> content) {
		super();
		this.result = result;
		this.content = content;
	}

	/**
	 * 処理結果をセットする。
	 * @param result 処理結果
	 * @since 1.1.0-1.1
	 */
	public void setResult(Integer result) {
		this.result = result;
	}

	/**
	 * コンテンツをセットする。
	 * @param content コンテンツ
	 * @since 1.1.0-1.1
	 */
	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	/**
	 * エラーコンテンツをセットする。
	 * @param errorsAsJson json形式のエラーメッセージ
	 * @since 1.1.0-1.1
	 */
	public void setErrors(JsonNode errorsAsJson) {
		content.put("errors", errorsAsJson);
	}

	/**
	 * エラーコンテンツをセットする。
	 * @param errorsAsJson json形式のエラーメッセージ
	 * @param message エラーメッセージ
	 * @since 1.1.0-1.1
	 */
	public void setErrors(JsonNode errorsAsJson, String message) {
		content.put("errors", errorsAsJson);
		content.put("error", message);
	}

	/**
	 * コンテンツレンダリング処理。
	 * @return JSON jsonオブジェクト
	 * @since 1.1.0-1.1
	 */
	public JsonNode render() {
		content.put("result", result);
		return Json.toJson(content);
	}
}
