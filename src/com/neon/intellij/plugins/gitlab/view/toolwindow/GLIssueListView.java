package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLNamespaceNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import javax.swing.tree.DefaultTreeModel;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GLIssueListView extends JPanel implements ProjectsHolder, ProjectIssuesHolder {

    private static final Integer DEFAULT_NAMESPACE_ID = -1;

    private final GLIController controller;

    private final FilteredTreeModel filteredModel = new FilteredTreeModel( new DefaultMutableTreeNode( "namespaces" ) );

    private final Tree tree = new Tree( filteredModel );

    public GLIssueListView( final GLIController controller ) {
        this.controller = controller;

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

    public < T > T[] getSelectedNodes( Class< T > clazz, Tree.NodeFilter< T > filter ) {
        return tree.getSelectedNodes( clazz, filter );
    }

    public void filter( final String author, final String assignee, final String filter, final boolean showClosedIssues ) {
        filteredModel.setFilter( filter );
        filteredModel.setShowClosedIssues( showClosedIssues );
        filteredModel.setAuthor( author );
        filteredModel.setAssignee( assignee );
        ((DefaultTreeModel) filteredModel.getTreeModel() ).reload();
    }

    @Override
    public void addProjects( List<GitlabProject> projects ) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

        root.removeAllChildren();
        if ( projects == null ) {
            return ;
        }

        Map< Integer, GLNamespaceNode> namespaceNodes = new HashMap<Integer, GLNamespaceNode>();

        for ( GitlabProject project : projects ) {
            GLNamespaceNode rootNodeFor = getRootNodeFor( project, root, namespaceNodes );
            GLProjectNode glProjectNode = new GLProjectNode(project);
            rootNodeFor.add( glProjectNode );
            ProgressManager.getInstance().run( new GetProjectIssuesTask( controller, glProjectNode, this ) );
        }

        tree.treeDidChange();
        tree.expandPath( new TreePath( root.getPath() ) );
    }

    private GLNamespaceNode getRootNodeFor( GitlabProject project, final DefaultMutableTreeNode root, final Map< Integer, GLNamespaceNode > namespaces ) {
        GitlabNamespace namespace = project.getNamespace();
        Integer namespaceId = namespace == null ? DEFAULT_NAMESPACE_ID : namespace.getId();

        GLNamespaceNode rootNode = namespaces.get( namespaceId );
        if ( rootNode == null ) {
            rootNode = new GLNamespaceNode(namespace);
            namespaces.put( namespaceId, rootNode );
            root.add( rootNode );
        }
        return rootNode;
    }

    @Override
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
