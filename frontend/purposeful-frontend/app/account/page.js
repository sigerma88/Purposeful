// By default, server components
// Since we want interaction with the user,
// we need to use client components
// see https://beta.nextjs.org/docs/rendering/server-and-client-components#when-to-use-server-vs-client-components
"use client";
import {
  Button,
  Flex,
  Heading,
  Stack,
  useColorMode,
  useColorModeValue,
} from "@chakra-ui/react";
import {
  ModifyDetails,
  ModifyPassword,
} from "@/components/ModifyAccountDetails";

export default function ModifyAccountDetailsPage() {
  const { colorMode, toggleColorMode } = useColorMode(); // TODO: Move the light/dark mode toggle button to the navigation header
  // TODO: Add the header and the navigation bar

  return (
    <Flex
      minH={"100vh"}
      align={"center"}
      justify={"center"}
      bg={useColorModeValue("gray.50", "gray.800")}>
      {/* TODO: Move the light/dark mode toggle button to the navigation header */}
      <Button onClick={toggleColorMode}>
        Toggle {colorMode === "light" ? "Dark" : "Light"}
      </Button>
      <Stack spacing={4} mx={"auto"} maxW={"2xl"} py={12} px={6}>
        <Heading as="h1" size="2xl" align="center">
          Modify your account information below
        </Heading>
        <ModifyDetails />
        <ModifyPassword />
      </Stack>
    </Flex>
  );
}
