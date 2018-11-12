package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.neon.intellij.plugins.gitlab.*;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import com.neon.intellij.plugins.gitlab.model.intellij.GLGroupNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GLIssueListView extends JPanel implements GIPGroupObserver, GIPProjectObserver, GIPIssueObserver {

    private static final Logger LOGGER = Logger.getLogger( GLIssueListView.class.getName() );

    private final FilteredTreeModel filteredModel = new FilteredTreeModel( new DefaultMutableTreeNode( "groups" ) );

    private final Tree tree = new Tree( filteredModel );

    public GLIssueListView(final OpenIssueEditorAction openIssueEditorAction,
                           final RefreshProjectIssuesAction refreshProjectIssuesAction,
                           final DeleteIssueAction deleteIssueAction,
                           final ChangeIssueStateAction changeIssueStateAction) {
        tree.setCellRenderer( new GLIssueListRenderer() );
        tree.addMouseListener( new GLIssueListMouseAdapter( openIssueEditorAction, refreshProjectIssuesAction,
                deleteIssueAction, changeIssueStateAction, tree ) );
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

    public void filter(final GIPUser author, final GIPUser assignee, final String filter, final boolean showClosedIssues, final boolean showEmptyNodes ) {
        filteredModel.setFilter( filter );
        filteredModel.setShowClosedIssues( showClosedIssues );
        filteredModel.setAuthor( author );
        filteredModel.setAssignee( assignee );
        filteredModel.setShowEmptyNodes( showEmptyNodes );
        ((DefaultTreeModel) filteredModel.getTreeModel() ).reload();
    }



    private final Map< Integer, GLGroupNode > groups = new HashMap<>();

    private final Map< Integer, GLProjectNode > projects = new HashMap<>();


    @Override
    public void accept( GIPGroup group ) {
        LOGGER.info( "[group] " + group.id + ". " + group.name );

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        if ( group.parent_id != null ) {
            root = groups.get(group.parent_id);
            if ( root == null ) {

                GIPGroup temp = new GIPGroup();
                temp.id = group.parent_id;

                root = new GLGroupNode( temp );

                groups.put( temp.id, (GLGroupNode) root);
            }
        }

        GLGroupNode glGroupNode = new GLGroupNode(group);
        groups.put( group.id, glGroupNode );

        root.add(glGroupNode);
    }

    @Override
    public void accept( GIPProject project ) {
        LOGGER.info( "[project] " + project.id + ". " + project.name );

        GLGroupNode glGroupNode = groups.get(project.namespace.id);
        if ( glGroupNode != null ) {
            GLProjectNode glProjectNode = new GLProjectNode(project);
            projects.put( project.id, glProjectNode );

            glGroupNode.add(glProjectNode);
        }
    }

    @Override
    public void accept(GIPIssue issue) {
        LOGGER.info( "[issue] " + issue.id + ". " + issue.title );

        GLProjectNode glProjectNode = projects.get(issue.project_id);
        if ( glProjectNode == null ) {
            GIPProject temp = new GIPProject();
            temp.id = issue.project_id;

            glProjectNode = new GLProjectNode( temp );

            projects.put( issue.project_id, glProjectNode );
        }

        GLIssueNode glIssueNode = new GLIssueNode( issue );

        glProjectNode.add( glIssueNode );
    }

}
