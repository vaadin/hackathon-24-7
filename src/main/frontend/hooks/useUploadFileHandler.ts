import { UploadElement, UploadRequestEvent } from '@vaadin/react-components';

type FileReceiver<TResult> = (file: File) => Promise<TResult>;

interface UploadFileHandlerOptions<TResult> {
  onSuccess?: (result: TResult) => void;
  onError?: (error: any) => void;
  uploadStatusMessage?: string;
  uploadErrorMessage?: string;
}

export function useUploadFileHandler<TResult>(
  receiver: FileReceiver<TResult>,
  options?: UploadFileHandlerOptions<TResult>,
) {
  const handleUploadRequest = async (e: UploadRequestEvent) => {
    e.preventDefault();

    const uploadRef = e.target as UploadElement;
    const file = e.detail.file;

    try {
      // Update file status and trigger render
      file.status = options?.uploadStatusMessage || 'Uploading...';
      file.uploading = true;
      uploadRef.files = [...uploadRef.files];

      // Upload the file
      const result = await receiver(e.detail.file);
      file.status = '';
      file.complete = true;
      (file as any).indeterminate = false;
      file.uploading = false;

      if (options?.onSuccess) {
        options.onSuccess(result);
      }
    } catch (error) {
      file.status = '';
      file.error = options?.uploadErrorMessage || 'Upload failed.';
      (file as any).indeterminate = false;
      file.uploading = false;

      if (options?.onError) {
        options.onError(error);
      }
    }

    // Trigger render after success or error
    uploadRef.files = [...uploadRef.files];
  };

  return {
    onUploadRequest: handleUploadRequest,
  };
}
