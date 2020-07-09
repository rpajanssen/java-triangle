package contracts.miscellaneous

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should not be able make an unauthorized call to the demo API"

    request {
        url "/api/admin/demo"
        method GET()
    }

    response {
        status FORBIDDEN()
        headers {
            contentType applicationJson()
        }
        body([
                code       : "0005"
        ])
    }
}
