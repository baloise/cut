package ch.basler.cat.client.backend;

import ch.basler.cat.client.backend.data.*;
import ch.basler.cat.client.backend.mock.MockDataService;

import java.util.Collection;

public class RestDataService extends AbstractRestDataService implements DataService {

    private static RestDataService INSTANCE;

    public static synchronized DataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestDataService();
        }
        return INSTANCE;
    }

    @Override
    public Collection<Application> getAllApplications() {
        return getAllData("applications", Application[].class);
    }

    @Override
    public void saveApplication(Application application) {
        if (application.isNewApplication()) {
            createData("applications", application, Application.class);
        } else {
            updateData("applications", application.getId(), application);
        }
    }

    @Override
    public void deleteApplication(long applicationId) {
        deleteData("applications", applicationId);
    }

    @Override
    public Application getApplicationById(long applicationId) {
        return getById("applications", applicationId, Application.class);
    }

    @Override
    public Collection<Responsible> getAllResponsibles() {
        return getAllData("responsibles", Responsible[].class);
    }

    @Override
    public void saveResponsible(Responsible responsible) {
        if (responsible.getId() <= 0) {
            createData("responsibles", responsible, Responsible.class);
        } else {
            updateData("responsibles", responsible.getId(), responsible);
        }
    }

    @Override
    public void deleteResponsible(long responsibleId) {
        deleteData("responsibles", responsibleId);
    }

    @Override
    public Responsible getResponsibleById(long responsibleId) {
        return getById("responsibles", responsibleId, Responsible.class);
    }

    @Override
    public Collection<CodeType> getAllCodeTypes() {
        return MockDataService.getInstance().getAllCodeTypes();
    }

    @Override
    public void saveCodeType(CodeType ct) {

    }

    @Override
    public void deleteCodeType(long codeTypeId) {

    }

    @Override
    public CodeType getCodeTypeById(long codeTypeId) {
        return null;
    }

    @Override
    public Collection<CodeValue> getAllCodeValues(CodeType codeType) {
        return null;
    }

    @Override
    public void saveCodeValue(CodeValue ct) {

    }

    @Override
    public void deleteCodeValue(String codeValueId) {

    }

    @Override
    public CodeValue getCodeValueById(String codeValueId) {
        return null;
    }
}