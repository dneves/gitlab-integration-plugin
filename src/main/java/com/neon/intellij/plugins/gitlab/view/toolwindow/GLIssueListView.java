package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.neon.intellij.plugins.gitlab.GIPGroupObserver;
import com.neon.intellij.plugins.gitlab.GIPProjectObserver;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.intellij.GLGroupNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GLIssueListView extends JPanel implements GIPGroupObserver, GIPProjectObserver {

    private static final Logger LOGGER = Logger.getLogger( GLIssueListView.class.getName() );

//    private static final Integer DEFAULT_NAMESPACE_ID = -1;

    private final FilteredTreeModel filteredModel = new FilteredTreeModel( new DefaultMutableTreeNode( "groups" ) );

    private final Tree tree = new Tree( filteredModel );

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

//    public < T > T[] getSelectedNodes( Class< T > clazz, Tree.NodeFilter< T > filter ) {
//        return tree.getSelectedNodes( clazz, filter );
//    }

//    public void filter( final String author, final String assignee, final String filter, final boolean showClosedIssues ) {
//        filteredModel.setFilter( filter );
//        filteredModel.setShowClosedIssues( showClosedIssues );
//        filteredModel.setAuthor( author );
//        filteredModel.setAssignee( assignee );
//        ((DefaultTreeModel) filteredModel.getTreeModel() ).reload();
//    }



    private final Map< Integer, GLGroupNode > groups = new HashMap<>();


    @Override
    public void accept( GIPGroup group ) {
        LOGGER.info( "[group] " + group.id + ". " + group.name );

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

        GLGroupNode glGroupNode = new GLGroupNode(group);
        groups.put( group.id, glGroupNode );

        root.add(glGroupNode);
    }

    @Override
    public void accept( GIPProject project ) {
        LOGGER.info( "[project] " + project.id + ". " + project.name );

        GLGroupNode glGroupNode = groups.get(project.namespace.id);
        if ( glGroupNode != null ) {
            glGroupNode.add( new GLProjectNode( project ) );
        }
    }

}
