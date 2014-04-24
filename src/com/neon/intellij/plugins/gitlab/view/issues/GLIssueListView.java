package com.neon.intellij.plugins.gitlab.view.issues;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.GLRootIssue;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;

public class GLIssueListView extends JPanel {

    private final GLRootIssue root = new GLRootIssue();

    private final Tree tree = new Tree( root );


    public GLIssueListView( final GLIController controller ) {
        tree.setCellRenderer( new GLIssueListRenderer() );
        tree.addMouseListener( new GLIssueListMouseAdapter( controller, this, tree ) );

        this.setLayout( new TableLayout(
                new double[] { TableLayout.FILL },
                new double[] { TableLayout.FILL }
        ) );

        final JBScrollPane scroller = new JBScrollPane( tree, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        this.add( scroller, new TableLayoutConstraints( 0, 0, 0, 0, TableLayout.FULL, TableLayout.FULL ) );
    }

    public void addProjects( List<GitlabProject> projects ) {
        root.removeAllChildren();

        if ( projects == null ) {
            return ;
        }

        for ( GitlabProject project : projects ) {
            root.add( new DefaultMutableTreeNode( project, true ) );
        }

        tree.treeDidChange();
        tree.expandPath( new TreePath( root.getPath() ) );
    }

    public void addIssues( final List<GitlabIssue> issues, final DefaultMutableTreeNode projectNode ) {
        projectNode.removeAllChildren();

        if ( issues == null ) {
            return ;
        }

        for ( GitlabIssue issue : issues ) {
            projectNode.add( new DefaultMutableTreeNode( issue, false ) );
        }

        tree.treeDidChange();
        tree.expandPath( new TreePath( projectNode.getPath() ) );
    }

}
