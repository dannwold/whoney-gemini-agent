# WHONEY Gemini Agent (Android)

## Overview

WHONEY Gemini Agent is a production-grade native Android application built with Kotlin and Jetpack Compose. It is a full AI agent system powered by Google Gemini, designed for system-level automation, tool execution, and memory persistence.

### SYSTEM OVERVIEW
- **WHONEY** as AI agent system (AgentController)
- **Gemini 1.5 Flash** as reasoning engine
- **RAG (Retrieval-Augmented Generation)** as memory layer
- **Tool Router** as action system
- **Termux Bridge** as OS control layer

---

### MESSAGE LIFECYCLE (FULL DETAIL)
1. **User Input:** Received via Jetpack Compose UI.
2. **Observe:** System retrieves relevant memories from RAG.
3. **Classify Intent:** Determine if it's a tool call, termux command, or natural language.
4. **Retrieve Memory (RAG):** Top-K relevant memories are fetched based on cosine similarity and importance.
5. **Plan Execution:** Agent decides the sequence of tools or Gemini calls.
6. **Execution:** Hybrid pipeline (Tools + Gemini) generates the response.
7. **Streaming Response:** Results are streamed to the UI in real-time.
8. **Reflection Step:** System evaluates output quality.
9. **Memory Storage:** Interaction is saved to Room (SQLite) and JSON.
10. **Embedding Update:** New memory is embedded and indexed for future retrieval.

---

### RAG SYSTEM
- **Embeddings:** 128-dimensional vectors representing interaction context.
- **Cosine Similarity:** Used to find the most relevant past interactions.
- **Weighted Retrieval:** Combines similarity, recency, and importance scores.
- **Efficiency:** Only Top-K memories are injected into the prompt; full history is never sent.

---

### TERMUX BRIDGE
- **HTTP Execution Flow:** Commands starting with `/termux`, `/exec`, or `/shell` are routed to a local server.
- **Command Lifecycle:** App -> POST request -> Termux Execution -> Response -> Chat UI.
- **Output Injection:** Termux results appear as system messages in the chat.

---

### TOOL ROUTER
- **Intent Detection:** Automatically routes commands to internal tools (Calculator, System Info, etc.).
- **Tool Chaining:** Supports executing multiple tools in sequence.
- **Structured Output:** Merges tool results into the final AI response.

---

### GEMINI INTEGRATION
- **API Usage:** Google AI SDK for Android.
- **Streaming Model:** Uses Gemini 1.5 Flash for low-latency reasoning.
- **Compressed Context Strategy:** Injects only the most relevant context to optimize token usage.

---

### UI ARCHITECTURE
- **Compose Structure:** Reactive, state-driven UI.
- **Reactive State Flow:** Uses Kotlin Flow and ViewModel for real-time updates.
- **Streaming Rendering:** Messages update token-by-token for a ChatGPT-like experience.

---

### MEMORY SYSTEM
- **Storage:** Room (SQLite) for structured data + JSON for flexibility.
- **Embedding Storage:** Vectors are stored as JSON strings in the database.
- **Retrieval Logic:** Optimized for mobile performance (<100ms target).

---

## Build Requirements
- Android Studio Iguana or newer
- Kotlin 1.9.0
- Gemini API Key (Set in `MainActivity.kt`)
