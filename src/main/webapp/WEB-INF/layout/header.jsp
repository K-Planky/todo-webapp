<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}"/> · Todo</title>
    <link rel="icon" type="image/svg+xml"
          href="data:image/svg+xml,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20512%20512%22%3E%3Crect%20x%3D%226%22%20y%3D%226%22%20width%3D%22500%22%20height%3D%22500%22%20rx%3D%2260%22%20fill%3D%22%231b2333%22%20stroke%3D%22%232a3342%22%20stroke-width%3D%2212%22%2F%3E%3Cg%20transform%3D%22translate%2864%2C64%29%20scale%280.75%29%22%20fill%3D%22%23ffffff%22%3E%3Cg%20transform%3D%22translate%280%2C512%29%20scale%280.05%2C-0.05%29%22%20stroke%3D%22none%22%3E%3Cpath%20d%3D%22M623%208237%20l-66%20-67%2087%20-10%20c151%20-18%20752%20-443%20843%20-595%2089%20-148%20-118%0A-252%20-389%20-195%20-202%2043%20-294%2038%20-503%20-29%20-267%20-86%20-298%20-113%20-144%20-129%20787%0A-82%20858%20-110%20934%20-369%2034%20-118%2046%20-141%2055%20-103%209%2039%2018%2025%2049%20-76%2036%20-120%2090%0A-203%2092%20-141%201%2047%2073%20-109%2088%20-193%208%20-44%2036%20-120%2062%20-170%2026%20-49%2071%20-153%2099%0A-230%2070%20-190%20360%20-599%20632%20-891%2027%20-28%20119%20-129%20205%20-225%20154%20-170%20205%20-210%0A178%20-139%20-7%2019%2054%20-25%20136%20-97%2082%20-73%20185%20-152%20229%20-174%2044%20-23%20125%20-78%20180%0A-122%20393%20-315%20724%20-470%201140%20-534%20203%20-31%20225%20-38%20285%20-100%20136%20-141%2029%20-230%0A-415%20-348%20-115%20-31%20-264%20-76%20-330%20-100%20-66%20-24%20-174%20-57%20-240%20-73%20-164%20-41%0A-179%20-51%20-186%20-129%20-4%20-37%20-26%20-88%20-49%20-113%20-49%20-53%20-76%20-272%20-38%20-310%2013%20-13%0A23%20-44%2023%20-70%200%20-63%2051%20-159%2099%20-184%2068%20-37%2081%20-23%2030%2032%20-51%2055%20-66%20154%20-27%0A178%2013%209%2017%2055%2010%20126%20-10%2090%20-4%20127%2027%20183%20l40%2070%20-12%20-63%20c-25%20-140%20136%0A-347%20271%20-347%20l51%200%20-45%2031%20c-54%2038%20-104%20131%20-104%20194%200%2026%20-8%2056%20-18%2066%20-45%0A47%20-41%20233%205%20221%2024%20-6%2050%20-12%2058%20-12%208%200%2015%20-13%2015%20-29%200%20-38%2062%20-71%20134%20-71%0A75%201%20110%2026%2095%2067%20-14%2035%2020%2046%2041%2013%207%20-11%2022%20-20%2034%20-20%2012%200%2010%2013%20-6%2032%0A-20%2024%20-50%2030%20-125%2023%20-121%20-12%20-86%2010%2098%2062%20378%20106%20758%20229%20850%20275%20l102%2052%0A38%20-41%20c38%20-40%2036%20-42%20-206%20-280%20-236%20-231%20-386%20-363%20-413%20-363%20-8%200%20-58%20-65%0A-113%20-144%20-110%20-159%20-220%20-275%20-335%20-352%20-80%20-53%20-105%20-198%20-37%20-220%2021%20-7%2022%0A0%204%2034%20-31%2058%20-11%2082%2069%2082%2043%200%2089%2019%20130%2053%2066%2056%20116%2037%2087%20-33%20-40%20-97%20-9%0A-200%2059%20-200%2011%200%205%2016%20-13%2037%20-55%2061%20-41%20135%2041%20208%20127%20113%20147%20135%20166%20185%0A29%2077%20260%20175%20260%20111%200%20-11%2019%20-30%2042%20-42%2049%20-26%20118%2023%20118%2084%200%2055%2036%2045%0A77%20-23%2040%20-65%2063%20-60%2063%2013%200%2065%20-62%2090%20-254%20102%20-116%208%20-166%2018%20-162%2035%209%2035%0A591%20575%20659%20611%20124%2065%20193%20250%20135%20364%20-23%2043%20-37%2082%20-33%2087%2030%2030%20504%2011%0A685%20-28%20237%20-50%20270%20-52%20270%20-18%200%2052%20-52%2095%20-170%20144%20l-120%2049%2080%200%20c44%200%0A132%20-12%20195%20-27%20148%20-35%20157%202%2022%2086%20l-94%2058%20117%2015%20c64%208%20128%206%20142%20-3%2014%0A-10%20105%20-73%20202%20-141%2097%20-68%20253%20-202%20346%20-299%20245%20-253%20675%20-632%20786%20-692%2092%0A-49%20361%20-49%20458%201%20l73%2037%2097%20-67%20c120%20-83%20253%20-106%20347%20-59%2036%2017%2076%2032%2090%2032%0A19%200%20149%20127%20149%20146%200%202%208%2034%2019%2070%2018%2064%2016%2068%20-110%20166%20-71%2056%20-129%20115%0A-129%20131%200%2024%2011%2022%2051%20-10%2051%20-40%20257%20-124%20390%20-159%20258%20-67%20401%20377%20155%20477%0A-36%2015%20-140%2075%20-231%20133%20-90%2058%20-169%20106%20-174%20106%20-11%200%20-298%20133%20-481%20224%0A-66%2032%20-105%2055%20-88%2049%20222%20-68%20772%20-213%20807%20-213%2025%200%208%2093%20-24%20133%20-46%2057%0A-147%20105%20-467%20225%20-148%2055%20-326%20124%20-398%20154%20-71%2030%20-224%2089%20-340%20131%20l-210%0A77%20400%20-10%20c409%20-10%20647%20-39%201142%20-140%20459%20-94%20430%20-1%20-51%20162%20l-191%2065%20124%0A-11%20c252%20-23%20234%2028%20-43%20123%20-132%2046%20-225%2060%20-539%2083%20-624%2045%20-837%2079%20-1017%0A165%20-118%2056%20-203%20125%20-150%20123%203%200%2055%20-14%20115%20-30%20250%20-67%20306%20-1%20100%20119%0Al-90%2053%20115%20-13%20c130%20-15%20145%204%2069%2092%20-66%2076%20-91%2089%20-784%20404%20-582%20264%20-834%0A366%20-1150%20466%20-60%2019%20-172%2070%20-247%20114%20-446%20258%20-1007%20448%20-1670%20566%20-267%2047%0A-355%2069%20-282%2069%2017%200%2020%207%208%2018%20-10%2010%20-124%2023%20-252%2028%20-128%206%20-247%2018%20-263%0A27%20-54%2031%20-394%20430%20-394%20462%200%209%20-54%2051%20-120%2093%20-125%2080%20-147%20104%20-80%2086%2022%0A-6%2040%20-3%2040%205%200%2049%20-239%20175%20-460%20242%20-132%2040%20-142%2047%20-99%2063%20105%2039%20-12%2063%0A-168%2034%20-392%20-73%20-735%20-21%20-921%20139%20-36%2031%20-38%2031%20-27%20-12%2014%20-55%2022%20-55%20-109%0A5%20-189%2086%20-333%2089%20-413%207z%20m1338%20-618%20c50%20-26%2050%20-72%200%20-98%20-76%20-41%20-138%2034%0A-77%2095%2029%2029%2029%2029%2077%203z%22%2F%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=Inter:wght@400;450;500;600&display=swap"
          rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@7.2.0/css/all.min.css" rel="stylesheet">

    <style>
        :root {
            /* forest-night palette (cool) */
            --neb-bg: #0b1019;
            --neb-surface: #121826;
            --neb-surface-2: #1b2333;
            --neb-field: #0e1320;
            --neb-border: #2a3342;
            --neb-text: #eef2f6;
            --neb-muted: #93a0b3;
            /* moonlight accent (teal) */
            --neb-accent: #2dd4bf;
            --neb-accent-strong: #0f766e;
            --neb-accent-hover: #0d9488;
            --neb-accent-active: #134e4a;
        }

        [data-bs-theme="dark"] {
            /* accent */
            --bs-primary: #2dd4bf;
            --bs-primary-rgb: 45, 212, 191;
            --bs-link-color: #5eead4;
            --bs-link-color-rgb: 94, 234, 212;
            --bs-link-hover-color: #99f6e4;
            --bs-link-hover-color-rgb: 153, 246, 228;

            /* monochrome neutrals */
            --bs-body-bg: var(--neb-bg);
            --bs-body-color: var(--neb-text);
            --bs-emphasis-color: #ffffff;
            --bs-secondary-color: var(--neb-muted);
            --bs-border-color: var(--neb-border);

            /* completed / success -> accent */
            --bs-success: #2dd4bf;
            --bs-success-rgb: 45, 212, 191;

            /* type */
            --bs-body-font-family: 'Inter', system-ui, -apple-system, sans-serif;

            /* shape, sharp */
            --bs-border-radius: .25rem;
            --bs-border-radius-lg: .375rem;
            --bs-border-radius-xl: .5rem;
        }

        /* night sky, flat, with a faint moon and horizon glow */
        body {
            background-color: var(--neb-bg);
            background-image: /* moon, bright disc */ radial-gradient(circle at 86% 12%, #f3f6f9 0, #eaeff5 26px, rgba(234, 239, 245, 0) 29px),
                /* moon, glow (single smooth falloff) */ radial-gradient(circle at 86% 12%, rgba(214, 228, 240, .13) 26px, rgba(200, 220, 235, .098) 34px, rgba(184, 210, 228, .072) 44px, rgba(168, 198, 222, .05) 58px, rgba(156, 190, 218, .034) 78px, rgba(150, 184, 214, .022) 105px, rgba(150, 184, 214, .013) 145px, rgba(150, 184, 214, .006) 200px, rgba(150, 184, 214, .002) 270px, rgba(150, 184, 214, 0) 320px),
                /* horizon glow */ radial-gradient(130% 26vh at 50% 100%, rgba(150, 180, 205, .12), transparent 72%);
            background-repeat: no-repeat;
            background-attachment: fixed;
            font-weight: 450;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        /* dark-theme chrome */
        ::selection {
            background: rgba(45, 212, 191, .30);
            color: #fff;
        }

        * {
            scrollbar-width: thin;
            scrollbar-color: #2a3342 transparent;
        }

        ::-webkit-scrollbar {
            width: 10px;
            height: 10px;
        }

        ::-webkit-scrollbar-track {
            background: transparent;
        }

        ::-webkit-scrollbar-thumb {
            background: #2a3342;
            border: 2px solid var(--neb-bg);
            border-radius: 2px;
        }

        ::-webkit-scrollbar-thumb:hover {
            background: #3a4658;
        }

        /* stars (flat, sharp), biased to the upper sky */
        /*
         * Stars live in two interleaved layers (::before = A, ::after = B) that
         * twinkle in mirror opposition: as A dims to .75, B brightens to 1 and
         * vice-versa, so the total light on screen stays constant, neighbouring
         * stars shimmer out of phase, with no global pulse. z-index -2 keeps both
         * behind the treeline; the prefers-reduced-motion block below freezes them.
         */
        body::before {
            content: "";
            position: fixed;
            inset: 0;
            z-index: -2;
            pointer-events: none;
            background-image:
                /* layer A, feature */
            radial-gradient(2.4px 2.4px at 14% 15%, #ffffff 0, rgba(255, 255, 255, .95) 38%, transparent 72%),
            radial-gradient(2.2px 2.2px at 38% 39%, #ffffff 0, rgba(255, 255, 255, .92) 40%, transparent 74%),
            radial-gradient(2.1px 2.1px at 24% 52%, #ffffff 0, rgba(255, 255, 255, .88) 42%, transparent 76%),
                /* layer A, mid */
            radial-gradient(1.7px 1.7px at 30% 9%, rgba(255, 255, 255, .80), transparent),
            radial-gradient(1.7px 1.7px at 56% 14%, rgba(255, 255, 255, .76), transparent),
            radial-gradient(1.7px 1.7px at 18% 43%, rgba(255, 255, 255, .72), transparent),
            radial-gradient(1.7px 1.7px at 63% 45%, rgba(219, 234, 254, .70), transparent),
            radial-gradient(1.7px 1.7px at 95% 35%, rgba(255, 255, 255, .68), transparent),
                /* layer A, faint */
            radial-gradient(1.3px 1.3px at 5% 12%, rgba(255, 255, 255, .65), transparent),
            radial-gradient(1.3px 1.3px at 33% 33%, rgba(255, 255, 255, .60), transparent),
            radial-gradient(1.3px 1.3px at 60% 29%, rgba(255, 255, 255, .60), transparent),
            radial-gradient(1.3px 1.3px at 11% 54%, rgba(255, 255, 255, .58), transparent),
            radial-gradient(1.3px 1.3px at 53% 37%, rgba(255, 255, 255, .60), transparent),
            radial-gradient(1.3px 1.3px at 78% 44%, rgba(255, 255, 255, .58), transparent),
            radial-gradient(1.3px 1.3px at 16% 8%, rgba(255, 255, 255, .58), transparent);
            background-repeat: no-repeat;
            animation: star-twinkle-a 10s ease-in-out infinite;
        }

        body::after {
            content: "";
            position: fixed;
            inset: 0;
            z-index: -2;
            pointer-events: none;
            background-image:
                /* layer B, feature */
            radial-gradient(2.3px 2.3px at 67% 11%, #e8f0ff 0, rgba(219, 234, 254, .92) 40%, transparent 74%),
            radial-gradient(2.3px 2.3px at 13% 38%, #fff4dd 0, rgba(255, 241, 214, .90) 40%, transparent 74%),
                /* layer B, mid */
            radial-gradient(1.7px 1.7px at 8% 29%, rgba(255, 255, 255, .78), transparent),
            radial-gradient(1.8px 1.8px at 45% 23%, rgba(219, 234, 254, .78), transparent),
            radial-gradient(1.8px 1.8px at 72% 35%, rgba(255, 241, 214, .72), transparent),
            radial-gradient(1.8px 1.8px at 50% 49%, rgba(255, 255, 255, .74), transparent),
            radial-gradient(1.7px 1.7px at 84% 51%, rgba(255, 255, 255, .70), transparent),
            radial-gradient(1.7px 1.7px at 40% 57%, rgba(255, 255, 255, .68), transparent),
                /* layer B, faint */
            radial-gradient(1.3px 1.3px at 22% 25%, rgba(255, 255, 255, .62), transparent),
            radial-gradient(1.3px 1.3px at 48% 8%, rgba(219, 234, 254, .62), transparent),
            radial-gradient(1.3px 1.3px at 75% 17%, rgba(255, 255, 255, .62), transparent),
            radial-gradient(1.3px 1.3px at 28% 60%, rgba(255, 255, 255, .56), transparent),
            radial-gradient(1.3px 1.3px at 66% 58%, rgba(219, 234, 254, .55), transparent),
            radial-gradient(1.3px 1.3px at 92% 56%, rgba(255, 255, 255, .55), transparent),
            radial-gradient(1.3px 1.3px at 70% 25%, rgba(255, 255, 255, .60), transparent);
            background-repeat: no-repeat;
            animation: star-twinkle-b 10s ease-in-out infinite;
        }

        @keyframes star-twinkle-a {
            0%, 100% { opacity: 1; }
            50% { opacity: .75; }
        }

        @keyframes star-twinkle-b {
            0%, 100% { opacity: .75; }
            50% { opacity: 1; }
        }

        /* forest treeline (three layered triangle patterns) */
        .night-scene {
            position: fixed;
            left: 0;
            right: 0;
            bottom: 0;
            height: 280px;
            z-index: -1;
            pointer-events: none;
        }

        .night-scene svg {
            display: block;
            width: 100%;
            height: 100%;
        }

        /* mist hazing the base of the treeline */
        .night-scene::after {
            content: "";
            position: absolute;
            left: 0;
            right: 0;
            bottom: 0;
            height: 130px;
            background: linear-gradient(to top, rgba(150, 180, 205, .13) 0, rgba(150, 180, 205, .045) 50%, transparent 100%);
            pointer-events: none;
        }

        h1, h2, h3, h4, h5, h6, .brand-name {
            font-family: 'Space Grotesk', system-ui, -apple-system, sans-serif;
            letter-spacing: -.02em;
            font-weight: 600;
        }

        /* brand lockup */
        .brand {
            display: inline-flex;
            align-items: center;
            gap: .6rem;
            text-decoration: none;
            color: var(--neb-text);
        }

        .brand-mark {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 2.4rem;
            height: 2.4rem;
            border-radius: .25rem;
            background: var(--neb-surface-2);
            border: 1px solid var(--neb-border);
            color: #fff;
            flex: none;
        }

        .brand-mark svg {
            width: 1.85rem;
            height: 1.85rem;
        }

        .brand-mark-lg {
            width: 3.6rem;
            height: 3.6rem;
            border-radius: .375rem;
            font-size: 1.5rem;
        }

        .brand-mark-lg svg {
            width: 2.85rem;
            height: 2.85rem;
        }

        .brand-name {
            font-size: 1.2rem;
            font-weight: 600;
        }

        /* navbar, flat */
        .navbar {
            background-color: var(--neb-surface);
            border-bottom: 1px solid var(--neb-border) !important;
        }

        /* buttons, flat */
        .btn {
            border-radius: .25rem;
            transition: background-color .12s ease, border-color .12s ease, color .12s ease;
        }

        .btn-primary {
            --bs-btn-color: #fff;
            --bs-btn-bg: var(--neb-accent-strong);
            --bs-btn-border-color: var(--neb-accent-strong);
            --bs-btn-hover-color: #fff;
            --bs-btn-hover-bg: var(--neb-accent-hover);
            --bs-btn-hover-border-color: var(--neb-accent-hover);
            --bs-btn-active-color: #fff;
            --bs-btn-active-bg: var(--neb-accent-active);
            --bs-btn-active-border-color: var(--neb-accent-active);
            font-weight: 500;
        }

        .btn:focus-visible {
            outline: none;
            box-shadow: 0 0 0 2px var(--neb-bg), 0 0 0 4px rgba(45, 212, 191, .55);
        }

        /* cards, flat, bordered, accent top edge */
        .card {
            background-color: var(--neb-surface);
            border: 1px solid var(--neb-border);
            border-top: 2px solid var(--neb-accent);
            border-radius: var(--bs-border-radius-lg);
        }

        /* list group / to-dos */
        .list-group-item {
            background-color: transparent;
            border-color: var(--neb-border);
            color: var(--neb-text);
        }

        /* form controls */
        .form-control {
            background-color: var(--neb-field);
            border-color: var(--neb-border);
            color: var(--neb-text);
            caret-color: var(--neb-accent);
            border-radius: .25rem;
            padding: .6rem .85rem;
        }

        .form-control:focus {
            background-color: var(--neb-field);
            color: var(--neb-text);
            border-color: var(--neb-accent);
            box-shadow: 0 0 0 2px rgba(45, 212, 191, .28);
        }

        .form-control::placeholder {
            color: var(--neb-muted);
        }

        .form-label {
            font-weight: 500;
            font-size: .9rem;
        }

        /* layout helpers */
        .container-narrow {
            max-width: 680px;
        }

        .form-narrow {
            max-width: 420px;
        }

        /* auth (login / register) hero */
        .auth-wrap {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 4vh 1rem 12vh;
        }

        /* to-do list */
        .todo-item {
            transition: background-color .12s ease, opacity .12s ease;
        }

        .todo-item:hover {
            background-color: rgba(255, 255, 255, .04);
        }

        .todo-item.is-done {
            opacity: .55;
        }

        /* title shrinks + wraps (incl. long unbroken words) so the row actions never get pushed out */
        .todo-title {
            min-width: 0;
            overflow-wrap: anywhere;
        }

        .todo-item form .btn:hover {
            color: var(--neb-accent) !important;
        }

        .btn-action {
            color: var(--neb-muted);
            opacity: .7;
            transition: opacity .12s, color .12s;
        }

        .todo-item:hover .btn-action {
            opacity: 1;
        }

        .btn-action:hover {
            color: var(--neb-text);
        }

        .btn-action-danger:hover {
            color: var(--bs-danger);
        }

        @media (prefers-reduced-motion: reduce) {
            *, *::before, *::after {
                animation-duration: .01ms !important;
                animation-iteration-count: 1 !important;
                transition-duration: .01ms !important;
            }
        }
    </style>
</head>
<body>
<div class="night-scene" aria-hidden="true">
    <svg width="100%" height="280" xmlns="http://www.w3.org/2000/svg">
        <defs>
            <pattern id="trees-back" width="368" height="280" patternUnits="userSpaceOnUse">
                <g fill="#0f1729">
                    <polygon points="5,280 23,192 41,280"/>
                    <polygon points="51,280 69,180 87,280"/>
                    <polygon points="97,280 115,198 133,280"/>
                    <polygon points="143,280 161,186 179,280"/>
                    <polygon points="189,280 207,194 225,280"/>
                    <polygon points="235,280 253,182 271,280"/>
                    <polygon points="281,280 299,196 317,280"/>
                    <polygon points="327,280 345,184 363,280"/>
                </g>
            </pattern>
            <pattern id="trees-mid" width="364" height="280" patternUnits="userSpaceOnUse">
                <g fill="#070c15">
                    <polygon points="3,280 26,150 49,280"/>
                    <polygon points="55,280 78,138 101,280"/>
                    <polygon points="107,280 130,162 153,280"/>
                    <polygon points="159,280 182,146 205,280"/>
                    <polygon points="211,280 234,158 257,280"/>
                    <polygon points="263,280 286,142 309,280"/>
                    <polygon points="315,280 338,166 361,280"/>
                </g>
            </pattern>
            <pattern id="trees-front" width="348" height="280" patternUnits="userSpaceOnUse">
                <g fill="#04060c">
                    <polygon points="2,280 29,104 56,280"/>
                    <polygon points="60,280 87,88 114,280"/>
                    <polygon points="118,280 145,122 172,280"/>
                    <polygon points="176,280 203,98 230,280"/>
                    <polygon points="234,280 261,128 288,280"/>
                    <polygon points="292,280 319,94 346,280"/>
                </g>
            </pattern>
        </defs>
        <rect width="100%" height="280" fill="url(#trees-back)"/>
        <rect width="100%" height="280" fill="url(#trees-mid)"/>
        <rect width="100%" height="280" fill="url(#trees-front)"/>
    </svg>
</div>
<c:if test="${not empty username}">
<nav class="navbar border-bottom mb-4 sticky-top">
    <div class="container container-narrow">
        <a class="brand" href="${pageContext.request.contextPath}/todos">
            <span class="brand-mark"><%@ include file="/WEB-INF/layout/logo.jsp" %></span>
            <span class="brand-name">Todo</span>
        </a>
        <div class="d-flex align-items-center gap-3">
            <span class="text-secondary small d-inline-flex align-items-center">
                <i class="fa-regular fa-user me-1"></i><c:out value="${username}"/>
            </span>
            <form method="post" action="${pageContext.request.contextPath}/logout" class="m-0 d-flex align-items-center">
                <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
                <button type="submit" class="btn btn-sm btn-link text-secondary text-decoration-none p-0">
                    Log out
                </button>
            </form>
        </div>
    </div>
</nav>
</c:if>

<c:choose>
<c:when test="${authLayout}">
<main class="auth-wrap">
    </c:when>
    <c:otherwise>
    <main class="container container-narrow pb-5">
        </c:otherwise>
        </c:choose>
