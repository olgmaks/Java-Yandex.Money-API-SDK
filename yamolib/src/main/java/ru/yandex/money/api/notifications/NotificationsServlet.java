package ru.yandex.money.api.notifications;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * <p/>
 * Created: 04.12.13 23:32
 * <p/>
 *
 * @author OneHalf
 */
public class NotificationsServlet extends HttpServlet {

    private static final Log LOG = LogFactory.getLog(NotificationsServlet.class);

    private final NotificationUtils notificationUtils = new NotificationUtils();

    private volatile static IncomingTransferListener listener;
    private volatile static String secret;

    public static void setListener(IncomingTransferListener listener) {
        NotificationsServlet.listener = listener;
    }

    public static void setSecret(String secret) {
        NotificationsServlet.secret = secret;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (listener == null || secret == null) {
            throw new IllegalStateException("servlet state is not initialised");
        }

        Map<String, String> parametersMap = createParametersMap(request);

        final String notificationType = parametersMap.get("notification_type") ;

        if (!"p2p-incoming".equals(notificationType)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported notification type: " + notificationType) ;
            return;
        }

        if (!notificationUtils.isHashValid(parametersMap, secret)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "SHA-1 hash verification failed") ;
            LOG.warn("SHA-1 hash verification failed: " + compileLogRecord(request, parametersMap, null)) ;
            return;
        }

        try {

            final boolean testNotification = Boolean.parseBoolean(request.getParameter("test_notification"));

            IncomingTransfer incomingTransfer = IncomingTransfer.createByParameters(parametersMap);

            // проверка факта того, что уведомление тестовое
            if (testNotification) {
                LOG.info("Test notification has received.") ;
                listener.processTestNotification(incomingTransfer) ;
            } else {
                listener.processNotification(incomingTransfer) ;
            }
            // response is HTTP 200 OK without content

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage()) ;
            LOG.warn(compileLogRecord(request, parametersMap, e)) ;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private Map<String, String> createParametersMap(HttpServletRequest req) {
        Map<String, String> parametersMap = new HashMap<String, String>();

        @SuppressWarnings("unchecked")
        Enumeration<String> parameterNames = req.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            parametersMap.put(paramName, req.getParameter(paramName));
        }
        return parametersMap;
    }

    private static String compileLogRecord(HttpServletRequest request, Map<String, String> parametersMap, Throwable e) {
        StringBuilder sb = new StringBuilder() ;
        if (e != null) {
            sb.append(e.getMessage()).append(" : ");
        }
        sb.append("HttpServletRequest={ IP:").append(request.getRemoteAddr())
                .append(':').append(request.getRemotePort())
                .append(" URL:").append(request.getRequestURL()) ;

        sb.append("} Parameters=").append(parametersMap).append(" }") ;
        return sb.toString();
    }
}
