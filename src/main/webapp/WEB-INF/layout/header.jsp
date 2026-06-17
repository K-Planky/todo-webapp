<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}"/> · Todo</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@7.2.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #fafafa;
        }

        .navbar {
            background-color: #fff;
        }

        .container-narrow {
            max-width: 680px;
        }

        .btn-action {
            color: var(--bs-secondary-color);
        }

        .btn-action:hover {
            color: var(--bs-emphasis-color);
        }

        .btn-action-danger:hover {
            color: var(--bs-danger);
        }

        .form-narrow {
            max-width: 420px;
        }
    </style>
</head>
<body>
<nav class="navbar border-bottom mb-4">
    <div class="container container-narrow">
        <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/todos">
            <i class="fa-regular fa-circle-check text-primary me-1"></i>Todo
        </a>
        <c:if test="${not empty username}">
            <div class="d-flex align-items-center gap-3">
                <span class="text-secondary small">
                    <i class="fa-regular fa-user me-1"></i><c:out value="${username}"/>
                </span>
                <form method="post" action="${pageContext.request.contextPath}/logout" class="m-0">
                    <button type="submit" class="btn btn-sm btn-link text-secondary text-decoration-none p-0">
                        Log out
                    </button>
                </form>
            </div>
        </c:if>
    </div>
</nav>

<main class="container container-narrow pb-5">