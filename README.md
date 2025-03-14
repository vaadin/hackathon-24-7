# Hilla Upload and Download Demo

Basic demo for testing file upload and download with Hilla. Contains a view that allows uploading a profile image through a browser-callable service, and downloading it again with a Spring REST endpoint.

Also includes a prototype for a [hook](https://github.com/vaadin/hackathon-24-7/blob/sascha/src/main/frontend/hooks/useUploadFileHandler.ts) that makes it easy to integrate the `Upload` component with a backend service:
```tsx
const uploadFileHandler = useUploadFileHandler(UploadService.uploadFile);

<Upload {...uploadFileHandler} />
```
