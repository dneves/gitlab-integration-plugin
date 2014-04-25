package com.neon.intellij.plugins.gitlab.view.issues;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GLIssueListView extends JPanel {

    private static final Integer DEAFAULT_NAMESPACE_ID = -1;

    private final Tree tree = new Tree( new DefaultMutableTreeNode( "namespaces" ) );


    public GLIssueListView( final GLIController controller ) {
        tree.setCellRenderer( new GLIssueListRenderer() );
        tree.addMouseListener( new GLIssueListMouseAdapter( controller, this, tree ) );
//        tree.setRootVisible( false );

        this.setLayout( new TableLayout(
                new double[] { TableLayout.FILL },
                new double[] { TableLayout.FILL }
        ) );

        final JBScrollPane scroller = new JBScrollPane( tree, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        this.add( scroller, new TableLayoutConstraints( 0, 0, 0, 0, TableLayout.FULL, TableLayout.FULL ) );
    }

    public void addProjects( List<GitlabProject> projects ) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

        root.removeAllChildren();
        if ( projects == null ) {
            return ;
        }

        Map< Integer, GLNamespaceNode > namespaceNodes = new HashMap<>();

        for ( GitlabProject project : projects ) {
            GLNamespaceNode rootNodeFor = getRootNodeFor( project, root, namespaceNodes );
            rootNodeFor.add(new GLProjectNode(project));
        }

        tree.treeDidChange();
        tree.expandPath( new TreePath( root.getPath() ) );
    }

    private GLNamespaceNode getRootNodeFor( GitlabProject project, final DefaultMutableTreeNode root, final Map< Integer, GLNamespaceNode > namespaces ) {
        GitlabNamespace namespace = project.getNamespace();
        Integer namespaceId = namespace == null ? DEAFAULT_NAMESPACE_ID : namespace.getId();

        GLNamespaceNode rootNode = namespaces.get( namespaceId );
        if ( rootNode == null ) {
            rootNode = new GLNamespaceNode(namespace);
            namespaces.put( namespaceId, rootNode );
            root.add( rootNode );
        }
        return rootNode;
    }

    public void addIssues( final List<GitlabIssue> issues, final GLProjectNode projectNode ) {
        projectNode.removeAllChildren();

        if ( issues == null ) {
            return ;
        }

        for ( GitlabIssue issue : issues ) {
            projectNode.add( new GLIssueNode( issue ) );
        }

        tree.treeDidChange();
        tree.expandPath( new TreePath( projectNode.getPath() ) );
    }

}
