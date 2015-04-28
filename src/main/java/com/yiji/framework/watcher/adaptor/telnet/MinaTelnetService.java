package com.yiji.framework.watcher.adaptor.telnet;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by SegeonTang on 2015/4/27.
 */
public class MinaTelnetService implements TelnetService{
    private int port;
    private IoAcceptor acceptor;

    public MinaTelnetService(int port) {
        this.port = port;
    }

    @Override
    public void init() throws Exception {
        acceptor = new NioSocketAcceptor();

        //acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName("UTF-8"))));

        acceptor.setHandler(  new MinaTelnetHandler() );

        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.bind(new InetSocketAddress(port));
    }

    @Override
    public void stop() {

    }
}
