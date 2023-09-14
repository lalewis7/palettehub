import Link from "next/link";
import Image from "next/image";
import { headers } from "next/headers";
import HeaderLink from "./header-link";

export default function Header(){
    const headersList = headers();
    const pathname = headersList.get("x-invoke-path") || "";
    return <> 
        <div id="header-navbar"  className="navbar navbar-expand-md bg-body-tertiary sticky-top" >
            <div className="container">
                <Link href="/" className="navbar-brand" id="header-logo-brand">
                    {/* <Logo size={28} height={28} color="#249cf3" className="me-2" /> */}
                    <Image src="/icon.svg" width={28} height={28} alt="Palette Hub Logo" className="me-2" />
                    <span className="lh-1">Palette Hub</span>
                </Link>
                <button aria-controls="headerOffcanvasContent" data-bs-target="#headerOffcanvasContent" data-bs-toggle="offcanvas" type="button" aria-label="Toggle navigation" className="navbar-toggler collapsed">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse d-none d-md-block">
                    <div className="navbar-nav me-0">
                        <HeaderLink type="popular" />
                        <HeaderLink type="new" />
                    </div>
                </div>
            </div>
        </div>
    </>
}
