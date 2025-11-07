import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { generate } from "./generator.js";

const server = new Server(
  { name: "mcp-generator", version: "1.0.0" },
  { capabilities: { tools: {} } }
);

// ✅ Use low-level handler that works in all SDK versions
(server as any).request = async (req: any) => {
  if (req.method === "tools/generate_artifacts") {
    const params = req.params || {};

    const result = generate({
      promptPath: String(params.promptPath || "src/test/resources/utilities/mcp/login.prompt.md"),
      platform: params.platform === "ios" ? "ios" : "android",
      javaRoot: params.javaRoot || "src/test/java",
      resRoot: params.resRoot || "src/test/resources"
    });

    // Standard MCP response format
    return {
      content: [
        {
          type: "text",
          text: JSON.stringify(result, null, 2)
        }
      ]
    };
  }

  // Fallback for unsupported methods
  return {
    content: [{ type: "text", text: "Unknown method: " + req.method }]
  };
};

// ✅ Connect server via stdio
server.connect(new StdioServerTransport());
