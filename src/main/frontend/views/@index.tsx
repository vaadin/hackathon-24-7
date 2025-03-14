import { _parent, AbstractModel } from '@vaadin/hilla-lit-form';
import { AutoCrud } from '@vaadin/hilla-react-crud';
import { UseFormResult } from '@vaadin/hilla-react-form';
import { useSignal } from '@vaadin/hilla-react-signals';
import { Button, Notification, NumberField, Upload, UploadElement, UploadRequestEvent } from '@vaadin/react-components';
import Document from 'Frontend/generated/com/cromoteca/algelo/entities/Document';
import DocumentModel from "Frontend/generated/com/cromoteca/algelo/entities/DocumentModel";
import { DocumentService } from "Frontend/generated/endpoints";

export default function HomeView() {
  function downloadRenderer({ item }: { item: Document }) {
    const { pdfId } = item;
    return pdfId ? (
      <a href={`/download/${pdfId}`} download>
        Download
      </a>
    ) : null;
  }

  const handleUploadRequest = async (e: UploadRequestEvent, form: UseFormResult<any>) => {
    console.log(form);
    const document = form.value as Document;
    e.preventDefault();

    const uploadRef = e.target as UploadElement;

    const pdfId = await DocumentService.storeDocument(document.id, e.detail.file);
    document.pdfId = pdfId;
    form.update();
    uploadRef.files = uploadRef.files.map((file) => {
      file.status = '';
      file.complete = true;
      return file;
    });
    setTimeout(() => {
      uploadRef.files = [];
    }, 2000);

    Notification.show(`Uploaded file: ${pdfId}`);
  };

  return (
    <section className="flex flex-col p-m gap-m">
      <AutoCrud
        service={DocumentService}
        model={DocumentModel}
        gridProps={{
          columnOptions: {
            pdfId: {
              renderer: downloadRenderer,
            }
          }
        }}
        formProps={{
          fieldOptions: {
            pdfId: {
              renderer: ({ field, form }) => <>
                <NumberField {...field} readonly hidden />
                <Upload maxFiles={1} onUploadRequest={e => handleUploadRequest(e, form)} />
              </>
            }
          }
        }}
      />
    </section>
  );
}
