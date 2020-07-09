package contracts.person

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should trigger the annoying filter resulting in an exception"

    request {
        url "/api/person"
        method GET()
        headers {
            header 'DumbHeader': 'whatever'
        }
    }

    response {
        status INTERNAL_SERVER_ERROR()
        headers {
            contentType applicationJson()
        }
        body([
                code : "9999"
        ])
    }
}
