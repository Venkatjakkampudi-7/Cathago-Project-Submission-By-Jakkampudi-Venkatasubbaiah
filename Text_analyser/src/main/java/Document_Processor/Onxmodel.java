package Document_Processor;

import ai.onnxruntime.*;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.util.*;

public class Onxmodel {
    private OrtSession se;
    
    public Onxmodel(String modelPath) throws OrtException {
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        this.se = env.createSession(modelPath, new OrtSession.SessionOptions());
    }
    
    public double compareDocuments(String doc1, String doc2) throws OrtException {
        float[] emb1 = getEmbedding(doc1);
        float[] emb2 = getEmbedding(doc2);

        if (emb1.length == 0 || emb2.length == 0) {
            System.err.println("Empty embeddings - cosine similarity cannot be computed.");
            return 0.0;
        }

        return cosineSimilarity(emb1, emb2);
    }

    private float[] getEmbedding(String text) throws OrtException {
        try (OrtEnvironment env = OrtEnvironment.getEnvironment()) {
            // Convert text into token IDs
            long[] tokenIds = tokenizeText(text);
    
            // Ensure tokenIds length matches model input (10 tokens)
            final int EXPECTED_TOKENS = 10;
            long[] paddedTokens = new long[EXPECTED_TOKENS];
            Arrays.fill(paddedTokens, 0); // Pad with 0s (or use special padding token if applicable)
    
            // Copy actual tokens, truncate if needed
            System.arraycopy(tokenIds, 0, paddedTokens, 0, Math.min(tokenIds.length, EXPECTED_TOKENS));
    
            // Create an ONNX tensor from token IDs
            try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, LongBuffer.wrap(paddedTokens), new long[]{1, EXPECTED_TOKENS})) {
                
                // Run the model and get the result
                OrtSession.Result result = this.se.run(Collections.singletonMap("input_ids", inputTensor));
    
                // Handle Optional<OnnxValue> correctly
                Optional<OnnxValue> optionalOutput = result.get("last_hidden_state");
                if (optionalOutput.isPresent()) { 
                    try (OnnxValue outputValue = optionalOutput.get()) {
                        if (!(outputValue instanceof OnnxTensor)) {
                            throw new OrtException(OrtException.OrtErrorCode.ORT_FAIL, "Output is not an OnnxTensor.");
                        }

                        OnnxTensor outputTensor = (OnnxTensor) outputValue;
                        float[][][] embeddings = (float[][][]) outputTensor.getValue(); // Get full tensor

                        return embeddings[0][0]; // Extract CLS token embedding
                    }
                } else {
                    throw new OrtException(OrtException.OrtErrorCode.ORT_FAIL, "No output found for last_hidden_state.");
                }

            }
        }
    }
    
    private double cosineSimilarity(float[] vec1, float[] vec2) {
        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normA += Math.pow(vec1[i], 2);
            normB += Math.pow(vec2[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)); // Normalize
    }

    private static long[] tokenizeText(String text) {
        // Simple dummy tokenization (You need a proper tokenizer like BERT tokenizer)
        return text.toLowerCase().split(" ").length > 0 
               ? text.toLowerCase().chars().asLongStream().toArray()
               : new long[]{101}; // Default token
    }

    private static float[] toFloatArray(long[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = (float) array[i];
        }
        return floatArray;
    }
}
