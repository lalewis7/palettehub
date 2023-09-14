'use client'

import Link from "next/link"
import { Fire, Stars } from "react-bootstrap-icons"
import { usePathname } from 'next/navigation'

export default function HeaderLink(props){
    const pathname = usePathname()

    const content = () => {
        switch (props.type){
            case "popular":
                return <>
                    <Fire />{' '}Popular
                </>
            case "new":
                return <>
                    <Stars />{' '}New
                </>
        }
    }

    return <>
        <Link href={`/feed/${props.type}`} className={"nav-link " +(pathname === "/feed/popular" ? "active" : "")}>
            {content()}
        </Link>
    </>
}