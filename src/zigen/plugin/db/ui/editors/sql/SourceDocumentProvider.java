package zigen.plugin.db.ui.editors.sql;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.StorageDocumentProvider;

public class SourceDocumentProvider extends StorageDocumentProvider {

	public SourceDocumentProvider() {}

	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
		SourceEditorInput input = (SourceEditorInput) element;
		String source = document.get();

		// MConnection cn = input.getConnection();
		// try
		// {
		// cn.getSqlExecutor().exec(new SqlCommand(source, ""), false);
		// }
		// catch(SqlExecutionException e)
		// {
		// throw new CoreException(new Status(4, "de.tl.jora", 0, e.getMessage(), null));
		// }


		/*
		 * ICompilableDBO dbo = JoraPlSqlEditor.findAndRefreshDocumentsDBO(cn, document); if(dbo != null) { input.setDBO(dbo); String msg = MCompileError.toMessage(dbo.getCompileErrors()); if(msg !=
		 * null) throw new CoreException(new Status(4, "de.tl.jora", 0, msg, null)); }
		 */
	}

	public IAnnotationModel getAnnotationModel(Object element) {
		return new AnnotationModel();
	}
}
