package frames;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class BareFrame extends JFrame
{
	private static enum Direction{NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST, NONE};
	private static final int DEFAULT_GRIP_WIDTH = 10;
	
	private int mGripWidth = BareFrame.DEFAULT_GRIP_WIDTH;
	private BareFrame.Direction mResizeDir = null;
	
	public BareFrame()
	{
		this.initializeBareFrame();
		return;
	}
	
	public BareFrame(final int gripWidth)
	{
		this.setGripWidth(gripWidth);
		this.initializeBareFrame();
		return;
	}
	
	public void setGripWidth(final int gripWidth)
	{
		if(gripWidth <= 0){
			this.mGripWidth = BareFrame.DEFAULT_GRIP_WIDTH;
		}else{
			this.mGripWidth = gripWidth;
		}
		return;
	}
	
	private void initializeBareFrame()
	{
		this.setUndecorated(true);
		this.addMouseListener(this.createResizeListener());
		this.addMouseMotionListener(this.createResizeMotionListener());
		this.setResizable(true);
		return;
	}
	
	private MouseListener createResizeListener()
	{
		return new MouseListener()
		{
			public void mouseClicked(MouseEvent e){ return; }
			public void mouseEntered(MouseEvent e){ return; }
			public void mouseExited(MouseEvent e){ return; }
			
			public void mousePressed(MouseEvent e)
			{
				if(e.getX() < mGripWidth && e.getY() < mGripWidth){
					mResizeDir = BareFrame.Direction.NORTHWEST;
				}else if(e.getX() < mGripWidth && e.getY() > (getHeight() - mGripWidth)){
					mResizeDir = BareFrame.Direction.SOUTHWEST;
				}else if(e.getX() > (getWidth() - mGripWidth) && e.getY() < mGripWidth){
					mResizeDir = BareFrame.Direction.NORTHEAST;
				}else if(e.getX() > (getWidth() - mGripWidth) && e.getY() > (getHeight() - mGripWidth)){
					mResizeDir = BareFrame.Direction.SOUTHEAST;
				}else if(e.getX() < mGripWidth){
					mResizeDir = BareFrame.Direction.WEST;
				}else if(e.getX() > (getWidth() - mGripWidth)){
					mResizeDir = BareFrame.Direction.EAST;
				}else if(e.getY() < mGripWidth){
					mResizeDir = BareFrame.Direction.NORTH;
				}else if(e.getY() > (getHeight() - mGripWidth)){
					mResizeDir = BareFrame.Direction.SOUTH;
				}else{
					mResizeDir = BareFrame.Direction.NONE;
				}
				return;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				mResizeDir = BareFrame.Direction.NONE;
				return;
			}
		};
	}
	
	private MouseMotionListener createResizeMotionListener()
	{
		return new MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e)
			{
				if(e.getX() < mGripWidth && e.getY() < mGripWidth){
					setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
				}else if(e.getX() < mGripWidth && e.getY() > getHeight() - mGripWidth){
					setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
				}else if(e.getX() > (getWidth() - mGripWidth) && e.getY() < mGripWidth){
					setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
				}else if(e.getX() > (getWidth() - mGripWidth) && e.getY() > getHeight() - mGripWidth){
					setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				}else if(e.getX() < mGripWidth){
					setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				}else if(e.getX() > (getWidth() - mGripWidth)){
					setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
				}else if(e.getY() < mGripWidth){
					setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				}else if(e.getY() > (getHeight() - mGripWidth)){
					setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
				}else{
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				return;
			}
			
			public void mouseDragged(MouseEvent e)
			{
				Rectangle bounds = getBounds();
				Point p = e.getPoint();
				
				if(mResizeDir == BareFrame.Direction.NORTHWEST){
					invalidate();
					SwingUtilities.convertPointToScreen(p, getContentPane());
					this.westernResize(bounds, p);
					this.northernResize(bounds, p);
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.SOUTHWEST){
					invalidate();
					bounds.height = p.y;
					// convertPointToScreen only needs to be done for the x-coordinate here.
					SwingUtilities.convertPointToScreen(p, getContentPane());
					this.westernResize(bounds, p);
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.NORTHEAST){
					invalidate();
					bounds.width = p.x;
					// convertPointToScreen only needs to be done for the y-coordinate here.
					SwingUtilities.convertPointToScreen(p, getContentPane());
					this.northernResize(bounds, p);
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.SOUTHEAST){
					invalidate();
					bounds.width = p.x;
					bounds.height = p.y;
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.WEST){
					invalidate();
					SwingUtilities.convertPointToScreen(p, getContentPane());
					this.westernResize(bounds, p);
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.EAST){
					invalidate();
					bounds.width = p.x;
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.NORTH){
					invalidate();
					SwingUtilities.convertPointToScreen(p, getContentPane());
					this.northernResize(bounds, p);
					setBounds(bounds);
					validate();
				}else if(mResizeDir == BareFrame.Direction.SOUTH){
					invalidate();
					bounds.height = p.y;
					setBounds(bounds);
					validate();
				}
			}
			
			private void westernResize(Rectangle bounds, Point p)
			{
				int currentX = bounds.x;
				int xDiff = bounds.x - p.x;
				bounds.x = p.x;
				bounds.width += xDiff;
				if(bounds.width < getMinimumSize().width){
					bounds.width = getMinimumSize().width;
					bounds.x = currentX;
				}
				return;
			}
			
			private void northernResize(Rectangle bounds, Point p)
			{
				int currentY = bounds.y;
				int yDiff = bounds.y - p.y;
				bounds.y = p.y;
				bounds.height += yDiff;
				if(bounds.height < getMinimumSize().height){
					bounds.height = getMinimumSize().height;
					bounds.y = currentY;
				}
				return;
			}
		};
	}
}
