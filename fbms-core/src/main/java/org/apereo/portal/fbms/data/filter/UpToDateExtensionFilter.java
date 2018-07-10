package org.apereo.portal.fbms.data.filter;

import org.apereo.portal.fbms.data.ExtensionFilterChain;
import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.FormRepository;
import org.apereo.portal.fbms.data.SubmissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * During HTTP GET requests for Submission objects, sets the <code>X-FBMS-UpToDate</code> header
 * which indicates whether the submission was made against the most recent version of the referenced
 * Form object.
 */
@Component
public class UpToDateExtensionFilter extends AbstractExtensionFilter<FbmsEntity> {

    public static final String UP_TO_DATE_HEADER_NAME = "X-FBMS-UpToDate";

    @Autowired
    private FormRepository formRepository;

    @Override
    public boolean appliesTo(FbmsEntity entity, HttpServletRequest request) {
        // Applies only to GET requests (further qualifications are evaluated below)
        return request.getMethod().equalsIgnoreCase("GET");
    }

    @Override
    public FbmsEntity doFilter(FbmsEntity entity, HttpServletRequest request,
            HttpServletResponse response, ExtensionFilterChain<FbmsEntity> chain) {

        final FbmsEntity rslt = chain.doFilter(entity);

        /*
         * We are only concerned with HTTP GET requests that return a single Submission object.
         */
        if (SubmissionEntity.class.isInstance(rslt)) {
            final SubmissionEntity submission = (SubmissionEntity) rslt;
            final FormEntity form = formRepository.findMostRecentByFname(submission.getId().getFname());
            final boolean upToDate = submission.getId().getVersion() == form.getId().getVersion();
            response.setHeader(UP_TO_DATE_HEADER_NAME, Boolean.toString(upToDate));
        }

        return rslt;

    }

}
