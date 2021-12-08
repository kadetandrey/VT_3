package by.bsuir.server.http;

import by.bsuir.server.http.end_point.EndPoint;
import by.bsuir.server.http.end_point.scanner.HttpEndPointScanner;
import by.bsuir.common.http.entity.HttpRequest;
import by.bsuir.common.http.entity.HttpResponse;
import by.bsuir.server.app.service.StudentFileService;
import by.bsuir.common.command_line.CommandLine;
import by.bsuir.common.http.entity.specification.exception.request.NotImplemented;
import by.bsuir.server.app.ServiceLocator;
import by.bsuir.server.app.dao.iface.IStudentFileDao;
import by.bsuir.server.app.dao.student_file.IStudentFileService;
import by.bsuir.server.app.dao.student_file.StudentFileDao;
import by.bsuir.server.app.handler.HttpRequestHandler;

import java.util.Map;

public class HttpRequestDispatcher {

    static {
        ServiceLocator.register(IStudentFileDao.class, new StudentFileDao());
        ServiceLocator.register(IStudentFileService.class, new StudentFileService());
    }

    private final Map<String, EndPoint<HttpRequest, HttpResponse>> endPoints;
    
    public HttpRequestDispatcher() {
        endPoints = HttpEndPointScanner.findIn(new HttpRequestHandler());
    }

    public HttpResponse dispatch(HttpRequest httpRequest) throws Exception {
        CommandLine.println(">>>>> Got request >>>>> \n\n" + httpRequest + "\n");
        EndPoint<HttpRequest, HttpResponse> endPoint = endPoints.get(httpRequest.getMethod() + httpRequest.getUrl());

        if(endPoint == null)
            throw new NotImplemented(httpRequest.getMethod() + " method not implemented yet.");

        HttpResponse httpResponse = endPoint.invoke(httpRequest);
        CommandLine.println("<<<<< Result response <<<<< \n\n" + httpResponse + "\n");
        return httpResponse == null ? HttpResponse.nullBody() : httpResponse;
    }
}
