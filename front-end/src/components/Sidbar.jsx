import React from "react";
import LogoutButton from "./LogoutButton";
import BoekLenenButton from "./BoekLenenButton";
import LeesgeschiedenisButton from "./LeesgeschiedenisButton";
export default function Sidebar() {
    return (
        <div className="w-64 h-screen fixed top-0 left-0 bg-white border-r border-sidebar-line">
            <div className="flex flex-col h-full">
                <nav className="flex-1 overflow-y-auto px-2 pt-4">
                    <ul className="space-y-1">

                        <li>
                            <BoekLenenButton />
                        </li>

                        <li>
                            <LeesgeschiedenisButton />
                        </li>


                    </ul>
                </nav>
                <div className="mt-auto mb-4 px-2">
                    <LogoutButton />
                </div>

            </div>
        </div>
    );
}