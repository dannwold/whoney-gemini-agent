# WHONEY Gemini Agent

WHONEY is a production-grade native Android AI agent built with Kotlin and Jetpack Compose. It uses Gemini 1.5 Flash as a reasoning engine, combined with a sophisticated RAG (Retrieval-Augmented Generation) memory system and a dynamic tool router.

## SYSTEM OVERVIEW
- **WHONEY Brain Loop**: The central controller following the Observe-Think-Plan-Act-Reflect-Store-Improve cycle.
- **Gemini 1.5 Flash**: Used as an external reasoning engine, not the primary brain.
- **RAG Memory**: A weighted retrieval system using local embeddings and cosine similarity.
- **Tool Router**: Dynamically routes intents to internal tools or external systems like Termux.
- **Termux Bridge**: Enables OS-level control via a local HTTP bridge.

---

## MESSAGE LIFECYCLE
1. **User Input**: Input received via Jetpack Compose UI.
2. **Observe**: System captures state, user input, and relevant local context.
3. **Classify Intent**: The Tool Router determines if the request is a general query, a tool execution, or a system command.
4. **Retrieve Memory (RAG)**: Top-K relevant memories are fetched using vector similarity + recency/importance weighting.
5. **Plan Execution**: The system decides whether to use Gemini, a local tool, or a hybrid pipeline.
6. **Tool/Gemini Execution**: The plan is executed; Gemini receives only the necessary context (never full history).
7. **Streaming Response**: Results are streamed back to the UI in real-time.
8. **Reflection**: The agent evaluates the quality of the output.
9. **Memory Storage**: The interaction is indexed and stored locally.
10. **Improvement**: Retrieval weights are adjusted based on the success of the interaction.

---

## RAG SYSTEM
The system avoids sending full chat history to Gemini to ensure performance and privacy. Instead:
- **Embeddings**: Generated using a lightweight local model (simulated in this version).
- **Cosine Similarity**: Measures the distance between the current query and stored memories.
- **Weighted Retrieval**: Memories are ranked by:
  - Similarity (50%)
  - Importance (30%)
  - Recency (20%)
- **Top-K**: Only the 5-10 most relevant snippets are used as context.

---

## TERMUX BRIDGE
Allows WHONEY to control the Android OS.
- **Execution**: Commands starting with `/termux`, `/exec`, or `/shell` are routed to the bridge.
- **Lifecycle**:
  1. POST request to `http://127.0.0.1:8080/run`.
  2. JSON payload contains the command.
  3. Response captured and injected into the chat as a SYSTEM message.

---

## TOOL ROUTER
Detects user intent using regex and semantic analysis (future).
- **Tool Chaining**: Can sequence multiple tools (e.g., File Reader -> Calculator).
- **Merging**: Outputs from different sources are merged into a final structured response.

---

## GEMINI INTEGRATION
- **Model**: Gemini 1.5 Flash.
- **Streaming**: Token-by-token rendering for a responsive feel.
- **Compressed Context**: Uses a summary and RAG snippets instead of raw history.

---

## UI ARCHITECTURE
- **Jetpack Compose**: Modern, reactive UI.
- **State Flow**: Unidirectional data flow from ViewModel to UI.
- **Dark Mode**: Default production-grade theme.

---

## MEMORY SYSTEM
- **Local JSON/SQLite**: Stores raw message data.
- **Vector Store**: Caches embeddings for fast retrieval (<100ms target).
