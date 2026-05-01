package MiniCodeJudgeSystem;

public class Config {
    // Change this to your hosted backend URL (e.g., https://zencode-api.onrender.com)
    // When running locally, use http://localhost:3000
    public static final String BACKEND_BASE_URL = "http://localhost:3000";
    
    public static final String API_URL = BACKEND_BASE_URL + "/api";
    public static final String WS_URL = BACKEND_BASE_URL.replace("http", "ws") + "/ws";
}
