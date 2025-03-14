## Attempt to integrate upload and download in AutoCRUD

Download works fine.

Upload somewhat works, but I had to patch `autoform-field.tsx` to access the form:

```diff
diff --git a/packages/ts/react-crud/src/autoform-field.tsx b/packages/ts/react-crud/src/autoform-field.tsx
index caddd51fe..96dfbbf27 100644
--- a/packages/ts/react-crud/src/autoform-field.tsx
+++ b/packages/ts/react-crud/src/autoform-field.tsx
@@ -126,7 +126,7 @@ export type FieldOptions = Readonly<{
    * }
    * ```
    */
-  renderer?(props: { field: CustomFormFieldProps }): JSX.Element;
+  renderer?(props: { field: CustomFormFieldProps; form: UseFormResult<AbstractModel> }): JSX.Element;
   /**
    * Validators to apply to the field. The validators are added to the form
    * when the field is rendered.
@@ -223,7 +223,7 @@ export function AutoFormField(props: AutoFormFieldProps): JSX.Element | null {
 
   if (options.renderer) {
     const customFieldProps = { ...field, disabled: props.disabled, label };
-    return options.renderer({ field: customFieldProps });
+    return options.renderer({ field: customFieldProps, form: form as UseFormResult<AbstractModel> });
   }
 
   const fieldProps: CommonFieldProps = {
```

### What's needed:

- Some file handling on the server to store the upload temporarily and confirm on form submission.
- Update the hidden field that holds the uploaded file id, without submitting.
- A checkbox to mark current file for deletion.

### Issues

Opened [#3349](https://github.com/vaadin/hilla/issues/3349).
