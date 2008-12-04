/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;

public class PlsqlAnnotationHover implements IAnnotationHover {

    public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
        return "";
    }

}
