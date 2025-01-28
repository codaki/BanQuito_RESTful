package ec.edu.monster.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Path("/images")
public class WSImage {

    private static final String IMAGE_DIR = System.getProperty("user.home") + File.separator + "payara-images" + File.separator;

    // Upload image using JSON
    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadImage(ImageRequest imageRequest) {
        try {
            // Ensure the directory exists
            File dir = new File(IMAGE_DIR);
            if (!dir.exists() && !dir.mkdirs()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create image directory: " + IMAGE_DIR)
                        .build();
            }

            // Decode the base64 image and save it as a file
            byte[] imageData = Base64.getDecoder().decode(imageRequest.getBase64Data());
            File file = new File(IMAGE_DIR + imageRequest.getFileName());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(imageData);
            }
            return Response.ok("Image uploaded successfully to " + file.getAbsolutePath()).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error uploading image: " + e.getMessage())
                    .build();
        }
    }

    // Download image as base64 JSON
    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_JSON)
    public Response downloadImage(@QueryParam("fileName") String fileName) {
        try {
            File file = new File(IMAGE_DIR + fileName);
            if (!file.exists()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("File not found: " + file.getAbsolutePath())
                        .build();
            }

            // Read the file and encode it as base64
            byte[] fileData = Files.readAllBytes(file.toPath());
            String base64Data = Base64.getEncoder().encodeToString(fileData);

            ImageResponse imageResponse = new ImageResponse(fileName, base64Data);
            return Response.ok(imageResponse).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error downloading image: " + e.getMessage())
                    .build();
        }
    }

    // DTOs for JSON requests and responses
    public static class ImageRequest {
        private String fileName;
        private String base64Data;

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getBase64Data() {
            return base64Data;
        }

        public void setBase64Data(String base64Data) {
            this.base64Data = base64Data;
        }
    }

    public static class ImageResponse {
        private String fileName;
        private String base64Data;

        public ImageResponse(String fileName, String base64Data) {
            this.fileName = fileName;
            this.base64Data = base64Data;
        }

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getBase64Data() {
            return base64Data;
        }

        public void setBase64Data(String base64Data) {
            this.base64Data = base64Data;
        }
    }
}
