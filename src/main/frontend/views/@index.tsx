import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {Upload, UploadElement, UploadI18n, VerticalLayout} from '@vaadin/react-components';
import {UploadService} from 'Frontend/generated/endpoints';
import {useComputed, useSignal} from '@vaadin/hilla-react-signals';
import {useUploadFileHandler} from 'Frontend/hooks/useUploadFileHandler';

export const config: ViewConfig = {
    menu: {order: 0, icon: 'line-awesome/svg/globe-solid.svg'},
    title: 'Upload demo',
};

const uploadI18n: UploadI18n = {
    ...new UploadElement().i18n,
    addFiles: {
        one: 'Upload profile image...',
        many: 'Upload profile image...',
    },
};

export default function UploadView() {
    const profileImageFile = useSignal<string | null>();
    const profileImageUrl = useComputed(() => {
        return profileImageFile.value ? `/download/${profileImageFile.value}` : null;
    });

    const uploadFileHandler = useUploadFileHandler(
        async (file) => {
            // Add delay to show loading progress state
            await new Promise((resolve) => setTimeout(resolve, 2000));
            return UploadService.uploadFile(file);
        },
        {
            onSuccess(fileId) {
                profileImageFile.value = fileId;
            },
        },
    );
    /* Otherwise should be as simple as this
    const uploadFileHandler = useUploadFileHandler(UploadService.uploadFile, {
      onSuccess(fileId) {
        profileImageFile.value = fileId;
      },
    });
    */

    return (
        <VerticalLayout theme="spacing padding">
            <h2>Profile</h2>
            {profileImageUrl.value ? (
                <img src={profileImageUrl.value} alt="Profile image" style={{width: '100px', height: '100px'}}/>
            ) : (
                <p>No profile image</p>
            )}
            <Upload {...uploadFileHandler} maxFiles={1} i18n={uploadI18n}></Upload>
        </VerticalLayout>
    );
}
