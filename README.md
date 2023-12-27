# coindeskExercise
實作內容:
1. 幣別 DB 維護功能。
2. 呼叫 coindesk 的 API。
3. 呼叫 coindesk 的 API,並進行資料轉換,組成新 API。 此新 API 提供:
甲、更新時間(時間格式範例:1990/01/01 00:00:00)。 - /lookup-all
㇠、幣別相關資訊(幣別,幣別中文名稱,以及匯率)。 - 中,英文轉換使用 language_id
4. 所有功能均須包含單元測試。
5. 排程同步匯率。 - Every 10 sec.


實作加分題
1. 完成- 印出所有 API 被呼叫 以及 呼叫外部 API 的 request and response body log。
2. swagger-ui
3. 完成- 多語系設計
4. 完成- 2 個以上 design pattern 實作 Builder and 
5. 能夠運行在 Docker
6. 完成- Error handling 處理 API response -- 完成
7. 加解密技術應用

測試API請下載coindesk.json並從Postman載入後即可測試.

