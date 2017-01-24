package jplee.jlib.util.rendering;

import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
//import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
//import static org.lwjgl.opengl.GL33.*;
//import static org.lwjgl.opengl.GL40.*;
//import static org.lwjgl.opengl.GL41.*;
//import static org.lwjgl.opengl.GL42.*;
//import static org.lwjgl.opengl.GL43.*;
//import static org.lwjgl.opengl.GL44.*;
//import static org.lwjgl.opengl.GL45.*;

import java.nio.ByteBuffer;

public class FrameBuffer {

	private int bufferWidth;
	private int bufferHeight;

	private int bufferId;
	private int bufferTextureId;
	private int depthBufferId;
	private int depthBufferTextureId;

	private FrameBuffer(int width, int height) {
		int major = glGetInteger(GL_MAJOR_VERSION);
		if(major < 3) {
			return;
		}
		this.bufferId = -1;
		this.bufferTextureId = -1;
		this.depthBufferId = -1;
		this.depthBufferTextureId = -1;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, this.bufferId);
		glViewport(0, 0, this.bufferWidth, this.bufferHeight);
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void dispose() {
		glDeleteFramebuffers(this.bufferId);
		if(this.bufferTextureId != -1)
			glDeleteTextures(this.bufferTextureId);
		glDeleteRenderbuffers(this.depthBufferId);
		if(this.depthBufferTextureId != -1)
			glDeleteTextures(this.depthBufferTextureId);
	}

	public int getBufferWidth() {
		return this.bufferWidth;
	}

	public int getBufferHeight() {
		return this.bufferHeight;
	}

	public int getBufferId() {
		return this.bufferId;
	}

	public int getBufferTextureId() {
		return this.bufferTextureId;
	}

	public int getDepthBufferId() {
		return this.depthBufferId;
	}

	public int getDepthBufferTextureId() {
		return this.depthBufferTextureId;
	}

	public static FrameBufferBuilder builder() {
		return new FrameBufferBuilder();
	}

	public static class FrameBufferBuilder {

		private FrameBuffer buffer;
		private int width;
		private int height;

		private boolean bufferTexture;
		private boolean depthTexture;

		private FrameBufferBuilder() {
			this.width = Display.getWidth();
			this.height = Display.getHeight();

			this.bufferTexture = false;
			this.depthTexture = false;
		}

		private int createFrameBuffer() {
			int frameBuffer = glGenFramebuffers();
			glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
			glDrawBuffer(GL_COLOR_ATTACHMENT0);
			return frameBuffer;
		}

		private int createBufferTexture(int width, int height) {
			int texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
			return texture;
		}

		private int createDepthBuffer(int width, int height) {
			int depthBuffer = glGenRenderbuffers();
			glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
			return depthBuffer;
		}

		private int createDepthTexture(int width, int height) {
			int texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT,
				(ByteBuffer) null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
			return texture;
		}

		public void setDimensions(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public void addBufferTexture() {
			this.bufferTexture = true;
		}

		public void addDepthTexture() {
			this.depthTexture = true;
		}

		public FrameBuffer build() {
			buffer = new FrameBuffer(width, height);
			buffer.bufferId = createFrameBuffer();
			buffer.depthBufferId = createDepthBuffer(width, height);

			if(this.bufferTexture)
				buffer.bufferTextureId = createBufferTexture(width, height);
			if(this.depthTexture)
				buffer.depthBufferTextureId = createDepthTexture(width, height);

			return buffer;
		}
	}
}
